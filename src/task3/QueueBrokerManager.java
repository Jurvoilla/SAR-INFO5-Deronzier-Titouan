package task3;

import java.util.HashMap;
import java.util.Map;

public class QueueBrokerManager {
    private static Map<String, QueueBroker> brokers = new HashMap<>();

    public static synchronized void registerBroker(String name, QueueBroker broker) {
        brokers.put(name, broker);
        System.out.println("[BrokerManager] Broker " + name + " registered.");
    }

    public static synchronized QueueBroker getQueueBroker(String name) {
        return brokers.get(name);
    }
}

