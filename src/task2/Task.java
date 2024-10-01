package task2;

import task1.Broker;

abstract class Task extends Thread {
    private Broker broker;
    private QueueBroker queueBroker;
    protected Runnable task;

    public Task(Broker broker, Runnable task) {
        this.broker = broker;
        this.task = task;
    }

    public Task(QueueBroker queueBroker, Runnable task) {
        this.queueBroker = queueBroker;
        this.task = task;
    }

    public Broker getBroker() {
        return broker;
    }

    public QueueBroker getQueueBroker() {
        return queueBroker;
    }

    public static Task getTask() {
        // Méthode pour obtenir la tâche courante, implémentation selon les besoins
        return null;
    }

    @Override
    public void run() {
        task.run(); // Exécute la tâche
    }
}
