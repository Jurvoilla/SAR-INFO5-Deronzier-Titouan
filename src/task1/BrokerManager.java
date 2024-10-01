package task1;

import java.util.HashMap;
import java.util.Map;

class BrokerManager {
    private static Map<String, Broker> brokers = new HashMap<>();

    public static synchronized void registerBroker(String name, Broker broker) {
        brokers.put(name, broker);
        System.out.println("[BrokerManager] Broker " + name + " registered.");
    }

    public static synchronized Broker getBroker(String name) {
        return brokers.get(name);
    }
}

