package task2;


public class LocalTask extends Task {

    public LocalTask(QueueBroker queueBroker, Runnable task) {
    	super(queueBroker, task);
    }

    @Override
    public void run() {
        task.run();
    }
}
