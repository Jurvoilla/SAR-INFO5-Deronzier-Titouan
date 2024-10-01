package task1;

class LocalChannel extends Channel {
    private CircularBuffer buffer;
    private boolean isDisconnected = false;
    private boolean isRemoteDisconnected = false;

    public LocalChannel(int bufferSize) {
        this.buffer = new CircularBuffer(bufferSize);
    }

    @Override
    public synchronized int read(byte[] bytes, int offset, int length) {
        while (buffer.empty() && !isRemoteDisconnected) {
            try {
                wait(); // Attend jusqu'à ce qu'il y ait des données à lire
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Channel read interrupted");
            }
        }

        if (isDisconnected && buffer.empty()) {
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
    public synchronized int write(byte[] bytes, int offset, int length) {
        if (isDisconnected) {
            throw new IllegalStateException("Channel is disconnected");
        }

        int bytesWritten = 0;
        for (int i = offset; i < offset + length && !buffer.full(); i++) {
            buffer.push(bytes[i]);
            bytesWritten++;
        }

        notifyAll(); // Notifie toute tâche bloquée en attente de lecture
        return bytesWritten;
    }

    @Override
    public synchronized void disconnect() {
        isDisconnected = true;
        notifyAll(); // Notifie toutes les tâches bloquées sur des opérations en attente
    }

    @Override
    public synchronized boolean disconnected() {
        return isDisconnected;
    }

    // Appelée lorsqu'une déconnexion à distance est détectée
    public synchronized void remoteDisconnect() {
        isRemoteDisconnected = true;
        notifyAll(); // Permet de libérer les lectures bloquées
    }
}



