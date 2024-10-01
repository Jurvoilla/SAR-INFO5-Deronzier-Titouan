package task1;

class LocalChannel extends Channel {
    private CircularBuffer writeBuffer;
    private CircularBuffer readBuffer;
    private boolean isDisconnected = false;
    private LocalChannel remoteChannel;

    public LocalChannel(int bufferSize, CircularBuffer writeBuffer, CircularBuffer readBuffer) {
        this.writeBuffer = writeBuffer; // Local Write, Remote Read
        this.readBuffer = readBuffer;  // Local Read, Remote Write
    }
    
    public synchronized void setRemoteChannel(LocalChannel remoteChannel) {
        this.remoteChannel = remoteChannel;
    }

    @Override
    public synchronized int read(byte[] bytes, int offset, int length) {
        while (readBuffer.empty() && !isDisconnected) {
            try {
                wait(); // Attend qu'il y ait des données à lire ou que le canal soit déconnecté
                System.out.println("I am Notify");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Channel read interrupted");
            }
        }

        if (isDisconnected && readBuffer.empty()) {
            throw new IllegalStateException("Channel is disconnected");
        }

        int bytesRead = 0;
        for (int i = 0; i < length && !readBuffer.empty(); i++) {
            bytes[offset + i] = readBuffer.pull();
            bytesRead++;
        }
        
        if (remoteChannel != null) {
            synchronized (remoteChannel) {
                remoteChannel.notifyAll();
            }
        }
        
        return bytesRead;
    }

    @Override
    public synchronized int write(byte[] bytes, int offset, int length) {
        if (isDisconnected) {
            throw new IllegalStateException("Channel is disconnected");
        }

        int bytesWritten = 0;
        for (int i = offset; i < offset + length && !writeBuffer.full(); i++) {
            writeBuffer.push(bytes[i]);
            bytesWritten++;
        }

        notifyAll();
        if (remoteChannel != null) {
            synchronized (remoteChannel) {
                remoteChannel.notifyAll();
            }
        }
        
        return bytesWritten;
    }

    public synchronized void receiveFromRemote(byte[] bytes, int offset, int length) {
        // Méthode appelée par le canal distant pour pousser des données dans le buffer de lecture
        int bytesReceived = 0;
        for (int i = offset; i < offset + length && !readBuffer.full(); i++) {
            readBuffer.push(bytes[i]);
            bytesReceived++;
        }
        notifyAll(); // Notifie toute tâche bloquée en attente de données à lire
        if (remoteChannel != null) {
            synchronized (remoteChannel) {
                remoteChannel.notifyAll();
            }
        }
    }

    @Override
    public synchronized void disconnect() {
        isDisconnected = true;
        notifyAll(); // Notifie toutes les tâches bloquées
    }

    @Override
    public synchronized boolean disconnected() {
        return isDisconnected;
    }
}




