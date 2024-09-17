package task1;

class LocalTask extends Task {
    private Runnable task;
    private Broker broker;

    public LocalTask(Broker broker, Runnable task) {
        super(broker, task);
        this.broker = broker;
        this.task = task;
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public Broker getBroker() {
        return broker;
    }
}



