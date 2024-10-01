package task1;

public class LocalTask extends Task {
    private Runnable task;

    public LocalTask(Broker broker, Runnable task) {
        super(broker, task);
        this.broker = broker;
        this.task = task;
    }

    @Override
    public void run() {
        task.run();
    }
}




