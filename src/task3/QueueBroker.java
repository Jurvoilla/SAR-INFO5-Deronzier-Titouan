package task3;

import java.util.HashMap;
import java.util.Map;

import task1.Broker;
import task1.BrokerManager;
import task1.Channel;
import task1.LocalBroker;


class QueueBroker {
    Broker broker;
    private Map<Integer, AcceptListener> acceptListeners = new HashMap<>();
    private EventPump eventPump;

    public QueueBroker(String name, EventPump eventPump) {
    	QueueBrokerManager.registerBroker(name, this);
        broker = new LocalBroker(name);
        this.eventPump = eventPump;
    }
    
    public String name() {
        return broker.name(); 
    }

    // Interface AcceptListener
    interface AcceptListener {
        void accepted(MessageQueue queue);
    }

    // Interface ConnectListener
    interface ConnectListener {
        void connected(MessageQueue queue);
        void refused();
    }

    // Méthode pour établir une connexion avec un autre broker
    public synchronized boolean bind(int port, AcceptListener listener) {
        if (acceptListeners.containsKey(port)) {
            return false; // Le port est déjà lié
        }
        acceptListeners.put(port, listener);
        return true;
    }

    // Méthode pour délier un AcceptListener d'un port
    public synchronized boolean unbind(int port) {
        return acceptListeners.remove(port) != null;
    }

    // Méthode pour tenter de se connecter à un autre broker
    public synchronized boolean connect(String remoteBrokerName, int port, ConnectListener listener) {
        // Simuler la connexion à un autre broker via BrokerManager
        QueueBroker remoteBroker = QueueBrokerManager.getQueueBroker(remoteBrokerName);
        if (remoteBroker == null) {
        	eventPump.postEvent(listener::refused);
            return false;
        }

        // Créer un MessageQueue et appeler le listener
        Channel channel = broker.connect(remoteBrokerName, port);
        MessageQueue queue = new MessageQueue(channel, eventPump);
        remoteBroker.acceptConnection(port);
        eventPump.postEvent(() -> listener.connected(queue));
        return true;
    }

    // Méthode pour accepter une connexion sur un port
    public synchronized void acceptConnection(int port) {
    	Channel channel = broker.accept(port);
    	MessageQueue queue = new MessageQueue(channel, eventPump);
        AcceptListener listener = acceptListeners.get(port);
        if (listener != null) {
        	eventPump.postEvent(() -> listener.accepted(queue));
        }
    }
}
