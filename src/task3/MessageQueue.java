package task3;

import task1.Channel;

class MessageQueue {
	
    interface Listener {
        void received(byte[] msg);
        void closed();
    }

	
    private Channel channel;
    private boolean isClosed = false;
    private EventPump eventPump;

    public MessageQueue(Channel channel, EventPump eventPump) {
        this.channel = channel;
        this.eventPump = eventPump;
    }

    public synchronized void setListener(Listener listener) {
        new Thread(() -> {
            try {
                while (!isClosed) {
                    // Lire les messages sur le channel
                    byte[] message = receive();
                    eventPump.postEvent(() -> listener.received(message));
                }
            } catch (Exception e) {
            	eventPump.postEvent(listener::closed);
            }
        }).start();
    }

    public boolean send(byte[] bytes) {
        return send(bytes, 0, bytes.length);
    }

    public boolean send(byte[] bytes, int offset, int length) {
        if (isClosed) {
            return false;
        }

        new Thread(() -> {
            try {
            	byte[] header = intToByteArray(length);
                channel.write(header, offset, header.length);
                channel.write(bytes, offset, length);
            } catch (Exception e) {
            	System.out.println("Message queue send Closed");
                close();
            }
        }).start();
        return true;
    }

    public synchronized byte[] receive() {
        if (isClosed) {
            throw new IllegalStateException("MessageQueue is closed");
        }

        byte[] header = new byte[4];
        channel.read(header, 0, header.length);
        int messageLength = byteArrayToInt(header);

        byte[] message = new byte[messageLength];
        channel.read(message, 0, messageLength);
        return message;
    }

    public synchronized void close() {
        isClosed = true;
        channel.disconnect();
    }

    public boolean closed() {
        return isClosed;
    }

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
