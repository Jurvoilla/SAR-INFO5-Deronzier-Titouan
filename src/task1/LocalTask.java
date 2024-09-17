package task1;

class LocalTask extends Task {
    private Broker broker;
    private Runnable task;

    LocalTask(Broker b, Runnable r) {
        super(b, r);
        this.broker = b;
        this.task = r;
    }

    @Override
    public void run() {
        // Exécuter la tâche dans un thread séparé
        task.run();
    }

    public static Broker getBroker() {
        return new LocalBroker("DefaultBroker");
    }
}


