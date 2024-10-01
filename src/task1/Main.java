package task1;

import java.util.concurrent.Semaphore;

public class Main {
	public static void main(String[] args) {
		test1();
		test2();
		test3();
    }
	
	public static void test1() {
        System.out.println("Test1");
        
        Broker broker1 = new LocalBroker("Broker1");
        Broker broker2 = new LocalBroker("Broker2");

        // Tâche 1 : Se connecte à Tâche 2 via le broker 1
        Task task1 = new LocalTask(broker1, () -> {
            Channel channel = broker1.connect("Broker2", 8080);
            byte[] message = "Hello from Task 1".getBytes();
            channel.write(message, 0, message.length);
            System.out.println("Task 1 sent message.");
            channel.disconnect();
            System.out.println("Channel Task 1 disconnect");
        });

        // Tâche 2 : Accepte la connexion via le broker 2
        Task task2 = new LocalTask(broker2, () -> {
            Channel channel = broker2.accept(8080);
            byte[] buffer = new byte[1024];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            System.out.println("Task 2 received: " + new String(buffer, 0, bytesRead));
            channel.disconnect();
            System.out.println("Channel Task 2 disconnect");
        });

        task1.start();
        task2.start();
	}
	
	public static void test2() {
        System.out.println("Test2");
        
        Broker broker3 = new LocalBroker("Broker3");
        Broker broker4 = new LocalBroker("Broker4");

        Task task1 = new LocalTask(broker3, () -> {
            Channel channel = broker3.connect("Broker4", 6000);
            byte[] message = "Hello from Task 3".getBytes();
            channel.write(message, 0, message.length);
            System.out.println("Task 3 sent message.");
            byte[] buffer = new byte[1024];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            System.out.println("Task 3 received: " + new String(buffer, 0, bytesRead));
            channel.disconnect();
            System.out.println("Channel Task 3 disconnect");
        });

        Task task2 = new LocalTask(broker4, () -> {
            Channel channel = broker4.accept(6000);
            byte[] buffer = new byte[1024];
            int bytesRead = channel.read(buffer, 0, buffer.length);
            System.out.println("Task 4 received: " + new String(buffer, 0, bytesRead));
            byte[] message = "Hello from Task 4".getBytes();
            channel.write(message, 0, message.length);
            System.out.println("Task 4 sent message.");
            channel.disconnect();
            System.out.println("Channel Task 4 disconnect");
        });

        task1.start();
        task2.start();
	}
	
	public static void test3() {
        System.out.println("Test3");
        
        Broker brokerBroken1 = new LocalBroker("BrokerBroken1");

        Task task1 = new LocalTask(brokerBroken1, () -> {
            Channel channel = brokerBroken1.connect("BrokerBroken", 7000);
        });

        task1.start();
	}
}

