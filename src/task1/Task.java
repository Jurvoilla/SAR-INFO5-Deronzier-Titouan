package task1;

abstract class Task extends Thread {
    Broker broker;
    Runnable runnable;

    // Constructeur pour associer un broker et une tâche à exécuter
    Task(Broker b, Runnable r) {
        this.broker = b;
        this.runnable = r;
    }

    // Retourne le broker associé à la tâche
    static Broker getBroker() {
        // Renvoie une instance de Broker
        return null; // À implémenter dans les sous-classes
    }
}

