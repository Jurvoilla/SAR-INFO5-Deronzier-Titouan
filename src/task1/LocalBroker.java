package task1;

import java.util.HashMap;
import java.util.Map;

class LocalBroker extends Broker {
    private Map<Integer, LocalChannel> channels = new HashMap<>(); // Map port -> channel

    LocalBroker(String name) {
        super(name);
    }

    @Override
    public Channel accept(int port) {
        // Simule l'acceptation d'une connexion locale
        return channels.get(port);
    }

    @Override
    public Channel connect(String name, int port) {
        // Simule la création d'une nouvelle connexion locale
        LocalChannel channel = new LocalChannel(new CircularBuffer(1024));
        channels.put(port, channel);
        return channel;
    }
}

