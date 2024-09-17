package task1;

public class Main {
    public static void main(String[] args) {
        // Créer deux brokers
        Broker broker1 = new LocalBroker("Broker1");
        Broker broker2 = new LocalBroker("Broker2");

        // Tâche 1 : Se connecte à Tâche 2 via broker1
        Task task1 = new LocalTask(broker1, () -> {
            Channel channel = broker1.connect("Broker2", 8080);
            byte[] message = "Hello from Task 1".getBytes();
            channel.write(message, 0, message.length);
        });

        // Tâche 2 : Accepte la connexion via broker2
        Task task2 = new LocalTask(broker2, () -> {
            Channel channel = broker2.accept(8080);
            byte[] buffer = new byte[1024];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            System.out.println("Received: " + new String(buffer, 0, bytesRead));
        });

        // Lancer les tâches
        task1.start();
        task2.start();
    }
}
