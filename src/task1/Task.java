package task1;

abstract class Task extends Thread {
    Task(Broker b, Runnable r) {}
    abstract Broker getBroker();
}
