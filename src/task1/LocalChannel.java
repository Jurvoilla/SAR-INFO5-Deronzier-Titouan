package task1;

class LocalChannel extends Channel {
    private CircularBuffer buffer;
    private boolean isConnected;

    public LocalChannel(int bufferSize) {
        this.buffer = new CircularBuffer(bufferSize);
        this.isConnected = true;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) {
        if (!isConnected) {
            throw new IllegalStateException("Channel is disconnected");
        }

        int bytesRead = 0;
        for (int i = 0; i < length && !buffer.empty(); i++) {
            bytes[offset + i] = buffer.pull();
            bytesRead++;
        }

        return bytesRead;
    }

    @Override
    public int write(byte[] bytes, int offset, int length) {
        if (!isConnected) {
            throw new IllegalStateException("Channel is disconnected");
        }

        int bytesWritten = 0;
        for (int i = 0; i < length; i++) {
            if (!buffer.full()) {
                buffer.push(bytes[offset + i]);
                bytesWritten++;
            } else {
                break; // stop writing when the buffer is full
            }
        }

        return bytesWritten;
    }

    @Override
    public void disconnect() {
        isConnected = false;
    }

    @Override
    public boolean disconnected() {
        return !isConnected;
    }
}


