package task1;

import java.util.concurrent.Semaphore;

public class Main {
	public static void main(String[] args) {
        Broker broker = new LocalBroker("Broker");

        Task task1 = new LocalTask(broker, () -> {
            Channel channel = broker.connect("Broker", 8080);
            byte[] message = "Hello from Task 1".getBytes();
            channel.write(message, 0, message.length);
            System.out.println("Task 1 sent message.");
            channel.disconnect();
        });

        Task task2 = new LocalTask(broker, () -> {
            Channel channel = broker.accept(8080);
            byte[] buffer = new byte[1024];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            System.out.println("Task 2 received: " + new String(buffer, 0, bytesRead));
        });

        task1.start();
        task2.start();
    }
}

