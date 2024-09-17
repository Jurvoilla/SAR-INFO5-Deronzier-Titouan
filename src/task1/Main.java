package task1;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        // Créer deux brokers
        Broker broker = new LocalBroker("Broker");

        // Tâche 1 : Se connecte à Tâche 2 via broker1
        Task task1 = new LocalTask(broker, () -> {
            Channel channel = broker.connect("Broker", 8080);
            byte[] message = "Hello from Task 1".getBytes();
            channel.write(message, 0, message.length);
            System.out.println("Send: " + new String(message, 0, message.length));
        });
        
        // Tâche 2 : Accepte la connexion via broker2
        Task task2 = new LocalTask(broker, () -> {
        	Channel channel = broker.accept(8080);
            byte[] buffer = new byte[1024];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            channel.disconnect();
            System.out.println("Received: " + new String(buffer, 0, bytesRead));
        });
        // Lancer les tâches
        task1.start();
        task2.start();
    }
}

