package task1;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

class LocalBroker extends Broker {
    private Map<Integer, LocalChannel> channels = new HashMap<>();

    public LocalBroker(String name) {
        super(name);
    }

    @Override
    public synchronized Channel accept(int port) {
        // Attend jusqu'à ce qu'un channel soit créé pour ce port
        while (!channels.containsKey(port)) {
            try {
                wait(); // Attente bloquante jusqu'à ce qu'une connexion soit disponible
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Gérer l'interruption
                return null;
            }
        }

        // Retourne le channel correspondant à la connexion établie
        return channels.remove(port);
    }

    @Override
    public synchronized Channel connect(String name, int port) {
        // Crée un channel pour la communication
        LocalChannel channel = new LocalChannel(1024); // Buffer de taille fixe, par exemple 1024 octets

        // Enregistre le channel dans la liste des connexions en attente sur le port
        channels.put(port, channel);

        // Notifie la tâche qui attend une connexion
        notifyAll();

        return channel;
    }
}


