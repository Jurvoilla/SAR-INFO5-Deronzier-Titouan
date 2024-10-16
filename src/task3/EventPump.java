package task3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class EventPump {

    // File d'attente des événements
    private BlockingQueue<Runnable> eventQueue = new LinkedBlockingQueue<>();
    private boolean running = true;

    // Démarre la pompe à événements dans un thread séparé
    public EventPump() {
        Thread eventThread = new Thread(() -> {
            while (running) {
                try {
                    // Récupère et exécute l'événement
                    Runnable event = eventQueue.take();
                    event.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        eventThread.start();
    }

    // Ajoute un événement dans la file d'attente
    public void postEvent(Runnable event) {
        eventQueue.offer(event); // Met l'événement dans la file d'attente
    }

    // Arrête la pompe à événements
    public void stop() {
        running = false;
        postEvent(() -> {}); // Événement vide pour arrêter proprement
    }
}
