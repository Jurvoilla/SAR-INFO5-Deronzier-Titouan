package task1;

class LocalChannel extends Channel {
    private CircularBuffer buffer;
    private boolean isDisconnected = false;

    LocalChannel(CircularBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) {
        if (buffer.empty()) {
            throw new IllegalStateException("Buffer is empty");
        }
        int bytesRead = 0;
        while (!buffer.empty() && bytesRead < length) {
            bytes[offset + bytesRead] = buffer.pull();
            bytesRead++;
        }
        return bytesRead;
    }

    @Override
    public int write(byte[] bytes, int offset, int length) {
        if (buffer.full()) {
            throw new IllegalStateException("Buffer is full");
        }
        int bytesWritten = 0;
        while (!buffer.full() && bytesWritten < length) {
            buffer.push(bytes[offset + bytesWritten]);
            bytesWritten++;
        }
        return bytesWritten;
    }

    @Override
    public void disconnect() {
        isDisconnected = true;
    }

    @Override
    public boolean disconnected() {
        return isDisconnected;
    }
}

