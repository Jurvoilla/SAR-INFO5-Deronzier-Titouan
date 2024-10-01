package task2;

import task1.Broker;
import task1.Channel;

class LocalQueueBroker extends QueueBroker{

    public LocalQueueBroker(Broker broker) {
    	super(broker);
    }

    public String name() {
        return broker.name(); // Utilise le nom du broker sous-jacent
    }

    public synchronized MessageQueue accept(int port) {
        // Utilise le broker sous-jacent pour accepter une connexion
        Channel channel = broker.accept(port);
        return new LocalMessageQueue(channel); // Retourne une MessageQueue basée sur le Channel
    }

    public synchronized MessageQueue connect(String name, int port) {
        // Utilise le broker sous-jacent pour établir une connexion
        Channel channel = broker.connect(name, port);
        return new LocalMessageQueue(channel); // Retourne une MessageQueue basée sur le Channel
    }
}

