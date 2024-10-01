package task2;

import task1.Channel;
import task1.Broker;
import task1.LocalBroker;

public class Main {
    public static void main(String[] args) {
        Broker broker1 = new LocalBroker("Broker1");
        Broker broker2 = new LocalBroker("Broker2");

        QueueBroker queueBroker1 = new LocalQueueBroker(broker1);
        QueueBroker queueBroker2 = new LocalQueueBroker(broker2);

        // Tâche 1 : Envoie un message via QueueBroker
        Task task1 = new LocalTask(queueBroker1, () -> {
            MessageQueue messageQueue = queueBroker1.connect("Broker2", 8080);
            byte[] message = "Hello, this is a message!".getBytes();
            messageQueue.send(message, 0, message.length);
            System.out.println("Task 1 sent a message.");
            messageQueue.close();
        });

        // Tâche 2 : Reçoit le message via QueueBroker
        Task task2 = new LocalTask(queueBroker2, () -> {
            MessageQueue messageQueue = queueBroker2.accept(8080);
            byte[] message = messageQueue.receive();
            System.out.println("Task 2 received: " + new String(message));
            messageQueue.close();
        });

        task1.start();
        task2.start();
    }
}

