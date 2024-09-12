package task1;

abstract class Channel {
    abstract int read(byte[] bytes, int offset, int lenght);
    abstract int write(byte[] bytes, int offset, int lenght);
    abstract void disconnected();
    abstract boolean isDisconnected();
}
