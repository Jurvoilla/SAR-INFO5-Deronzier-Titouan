package task1;

import java.util.HashMap;
import java.util.Map;

abstract class Task extends Thread {
	protected Broker broker;
    protected Runnable runnable;

    private final static Map<Long, Broker> brokers = new HashMap<>();

    // Constructeur pour associer un broker et une tâche à exécuter
    Task(Broker b, Runnable r) {
        this.broker = b;
        this.runnable = r;
    }

    // Retourne le broker associé à la tâche
    public static Broker getBroker() {
        // Renvoie une instance de Broker du thread en question
    	long threadID = Thread.currentThread().getId();
    	if(brokers.containsKey(threadID)) {
    		throw new IllegalStateException("Thread dont exist");
    	}
        return brokers.get(threadID); 
    }
}

