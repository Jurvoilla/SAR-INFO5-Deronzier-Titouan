package task1;

import java.util.HashMap;
import java.util.Map;

class LocalBroker extends Broker {
    private final Map<Integer, LocalChannel> channels = new HashMap<>();

    public LocalBroker(String name) {
        super(name);
    }

    @Override
    public synchronized Channel accept(int port) {
        // Attend jusqu'à ce qu'un channel soit créé pour ce port
        while (!channels.containsKey(port)) {
            try {
                System.out.println("[Broker: " + this + "] Waiting for connection on port " + port);
                wait(); // Bloque jusqu'à ce qu'un connect soit fait sur ce port
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null; // Retourne null en cas d'interruption
            }
        }

        // Un canal est disponible, on le retire de la map et le retourne
        return channels.remove(port);
    }

    @Override
    public synchronized Channel connect(String name, int port) {
        // Crée un canal pour la communication
        LocalChannel channel = new LocalChannel(1024); // Taille du buffer 1024 octets
        channels.put(port, channel);  // Enregistre le canal dans la map des connexions

        // Notifie la tâche qui attend une connexion via accept()
        notifyAll();
        return channel;
    }
}



