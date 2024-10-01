package task1;

import java.util.HashMap;
import java.util.Map;

class LocalBroker extends Broker {
    private final Map<Integer, Channel[]> channels = new HashMap<>(); // 2 Channels: [LocalWrite, RemoteWrite]

    public LocalBroker(String name) {
        super(name);
        BrokerManager.registerBroker(name, this); // Enregistrer le broker
    }

    @Override
    public synchronized Channel accept(int port) {
        // Attendre que le channel correspondant soit créé pour ce port
        while (!channels.containsKey(port)) {
            try {
                System.out.println("[Broker: " + this + "] Waiting for connection on port " + port);
                wait(); // Bloque jusqu'à ce qu'un channel soit créé pour ce port
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null; // Retourne null en cas d'interruption
            }
        }

        // Retourne le canal pour écrire du côté local
        return channels.get(port)[0]; // LocalWrite
    }

    @Override
    public synchronized Channel connect(String remoteBrokerName, int port) {
        System.out.println("[Broker: " + this + "] Connecting to broker " + remoteBrokerName + " on port " + port);

        // Rechercher le broker distant via le BrokerManager
        Broker remoteBroker = BrokerManager.getBroker(remoteBrokerName);
        if (remoteBroker == null) {
            System.out.println("[Broker: " + this + "] No such remote broker found.");
            return null;
        }

        // Crée deux channels, un pour chaque côté
        int bufferSize = 1024;
        CircularBuffer cb1 = new CircularBuffer(bufferSize);
        CircularBuffer cb2 = new CircularBuffer(bufferSize);
        LocalChannel localChannel = new LocalChannel(1024, cb1, cb2); // Ecriture locale, lecture distante
        LocalChannel remoteChannel = new LocalChannel(1024, cb2, cb1); // Ecriture distante, lecture locale
        localChannel.setRemoteChannel(remoteChannel);
        remoteChannel.setRemoteChannel(localChannel);
        

        // Établir la connexion bidirectionnelle
        synchronized (remoteBroker) {
            ((LocalBroker) remoteBroker).channels.put(port, new Channel[]{remoteChannel, localChannel});
            remoteBroker.notifyAll(); // Notifie le broker distant qu'une connexion est prête
        }

        // Enregistrer les deux canaux localement
        channels.put(port, new Channel[]{localChannel, remoteChannel}); // LocalWrite, RemoteWrite

        return localChannel; // Retourne le canal local pour l'écriture
    }
}



