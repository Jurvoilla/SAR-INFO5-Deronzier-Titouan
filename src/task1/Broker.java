package task1;

abstract class Broker {
    Broker(String name) {}
    abstract Channel accept(int port);
    abstract Channel connect(String name, int port);
}