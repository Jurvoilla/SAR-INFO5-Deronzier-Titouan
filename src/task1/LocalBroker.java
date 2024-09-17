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
        System.out.println("[Broker: " + this + "] Accepting on port " + port);

        // Attend jusqu'à ce qu'un channel soit créé pour ce port
        while (!channels.containsKey(port)) {
            try {
                System.out.println("[Broker: " + this + "] Waiting for connection on port " + port + ". Current channels: " + channels);
                wait(); // Attente bloquante jusqu'à ce qu'une connexion soit disponible
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Gérer l'interruption
                return null;
            }
        }

        // Un channel est disponible, on le retire de la map et le retourne
        System.out.println("[Broker: " + this + "] Channel found for port " + port + ". Proceeding with connection.");
        return channels.remove(port);
    }

    @Override
    public synchronized Channel connect(String name, int port) {
        System.out.println("[Broker: " + this + "] Connecting to port " + port + " on broker " + name);

        // Crée un channel pour la communication
        LocalChannel channel = new LocalChannel(1024); // Buffer de taille fixe, par exemple 1024 octets

        // Enregistre le channel dans la liste des connexions en attente sur le port
        channels.put(port, channel);
        System.out.println("[Broker: " + this + "] Channel added to channels map for port " + port + ". Channels now: " + channels);

        // Notifie la tâche qui attend une connexion
        notifyAll(); // Réveiller le thread dans accept() qui attend une connexion
        System.out.println("[Broker: " + this + "] Notified waiting threads.");

        // Retourne immédiatement le channel sans attente supplémentaire
        return channel;
    }
}


