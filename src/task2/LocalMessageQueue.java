package task2;

import task1.Channel;

class LocalMessageQueue extends MessageQueue {
    private Channel channel;
    private boolean isClosed = false;

    public LocalMessageQueue(Channel channel) {
        this.channel = channel;
    }

    @Override
    public synchronized void send(byte[] bytes, int offset, int length) {
        if (isClosed) {
            throw new IllegalStateException("MessageQueue is closed");
        }

        // Envoie d'abord la taille du message (en-tête)
        byte[] header = intToByteArray(length); // Convertir la taille du message en tableau d'octets
        channel.write(header, 0, header.length);

        // Ensuite, envoie le message lui-même
        channel.write(bytes, offset, length);
    }

    @Override
    public synchronized byte[] receive() {
        if (isClosed) {
            throw new IllegalStateException("MessageQueue is closed");
        }

        // Lire d'abord l'en-tête pour connaître la taille du message
        byte[] header = new byte[4]; // Taille d'un int en octets
        channel.read(header, 0, header.length);
        int messageLength = byteArrayToInt(header);

        // Ensuite, lire le message complet
        byte[] message = new byte[messageLength];
        channel.read(message, 0, messageLength);

        return message; // Retourne le message complet
    }

    @Override
    public synchronized void close() {
        isClosed = true;
        channel.disconnect();
    }

    @Override
    public synchronized boolean closed() {
        return isClosed;
    }

    // Méthodes utilitaires pour convertir un int en tableau d'octets et vice versa
    private byte[] intToByteArray(int value) {
        return new byte[] {
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value
        };
    }

    private int byteArrayToInt(byte[] bytes) {
        return (bytes[0] << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
    }
}
