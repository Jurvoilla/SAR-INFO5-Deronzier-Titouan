package task3;

public class Main {
    public static void main(String[] args) {
        // Créer une pompe à événements
        EventPump eventPump = new EventPump();

        QueueBroker broker1 = new QueueBroker("Broker1", eventPump);
        QueueBroker broker2 = new QueueBroker("Broker2", eventPump);

        // Tâche 1 : Attend une connexion entrante sur un port
        broker1.bind(8080, new QueueBroker.AcceptListener() {
            @Override
            public void accepted(MessageQueue queue) {
                System.out.println("Task 1: Connection accepted.");
                queue.setListener(new MessageQueue.Listener() {
                    @Override
                    public void received(byte[] msg) {
                        System.out.println("Task 1 received: " + new String(msg));
                    }

                    @Override
                    public void closed() {
                        System.out.println("Task 1: Connection closed.");
                    }
                });
            }
        });

        // Tâche 2 : Tente de se connecter à Task 1
        broker2.connect("Broker1", 8080, new QueueBroker.ConnectListener() {
            @Override
            public void connected(MessageQueue queue) {
                System.out.println("Task 2: Connected to Task 1.");
                queue.send("Hello from Task 2".getBytes());
            }

            @Override
            public void refused() {
                System.out.println("Task 2: Connection refused.");
            }
        });

        // Arrête la pompe à événements après un certain temps
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Temps d'attente avant d'arrêter
                eventPump.stop();
                System.out.println("Event pump stopped.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}


