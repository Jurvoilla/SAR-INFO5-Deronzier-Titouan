abstract class QueueBroker {
    QueueBroker (String name);

    interface AccectListener {
        void accepted(MessageQueue queue);
    }
    boolean lind(int port, AcceptListener listener);
    boolean unbind(int port);

    interface ConnectListener {
        void connected(MessageQueue queue);
        void refused();
    }
    boolean connect(String name, int port, Connected listener);
}

abstract class MessageQueue {
    interface Listener {
        void received(byte[] msg);
        void closed();
    }
    void setListener(Listener l);

    boolean send(byte[] bytes);
    boolean send(byte[] bytes, int offset, int length);

    void close();
    boolean closed();
}

------------------------------------------------SPEC TASK 3-------------------------------------------------------------

1. Spécification de QueueBroker 
Le QueueBroker est responsable de la gestion des connexions entre les tâches pour l'envoi et la réception de messages via des listeners. Il permet à une tâche d'attendre des connexions entrantes (via un AcceptListener) ou d'établir des connexions sortantes (via un ConnectListener).

Méthodes et interfaces de QueueBroker :
Constructeur :

QueueBroker(String name) : Crée un broker identifié par un nom unique.
AcceptListener (Interface interne) :

void accepted(MessageQueue queue) : Appelé lorsqu'une connexion entrante est acceptée avec succès et qu'un MessageQueue est prêt à être utilisé.
Méthodes de QueueBroker :

boolean bind(int port, AcceptListener listener) :
Associe un AcceptListener à un port donné. Le listener est appelé lorsqu'une connexion entrante est acceptée.
Retourne true si le port est correctement associé, sinon false (si le port est déjà utilisé, par exemple).
boolean unbind(int port) :
Libère le port précédemment associé, arrêtant l'attente de connexions entrantes.
Retourne true si le port est libéré avec succès, sinon false.
ConnectListener (Interface interne) :

void connected(MessageQueue queue) : Appelé lorsqu'une connexion sortante est établie avec succès, avec un MessageQueue prêt à l'usage.
void refused() : Appelé si la connexion est refusée ou échoue.
Méthodes de connexion :

boolean connect(String name, int port, ConnectListener listener) :
Tente d'établir une connexion avec le broker nommé et le port spécifié.
Si la connexion est réussie, le listener.connected() est appelé avec le MessageQueue correspondant. Sinon, listener.refused() est appelé.
Retourne true si la tentative de connexion est initiée avec succès, sinon false.
2. Spécification de MessageQueue
Le MessageQueue gère l'envoi et la réception de messages via un canal de communication. Les messages sont envoyés et reçus en tant qu'événements déclenchés par un Listener.

Méthodes et interfaces de MessageQueue :
Listener (Interface interne) :

void received(byte[] msg) : Appelé lorsqu'un message complet est reçu via le MessageQueue.
void closed() : Appelé lorsque la connexion est fermée ou que le MessageQueue est déconnecté.
Méthodes de MessageQueue :

void setListener(Listener l) :

Associe un Listener au MessageQueue. Le Listener est notifié lorsque des messages sont reçus ou lorsque la connexion est fermée.
boolean send(byte[] bytes) :

Envoie un message complet représenté par le tableau d'octets bytes.
Retourne true si le message est envoyé avec succès, sinon false (si le MessageQueue est fermé, par exemple).
boolean send(byte[] bytes, int offset, int length) :

Envoie une partie d'un message à partir du tableau d'octets bytes, en commençant à offset et pour une longueur de length.
Retourne true si le message est envoyé avec succès, sinon false.
void close() :

Ferme la connexion et libère les ressources associées au MessageQueue.
Appelle également le Listener.closed() pour notifier que la connexion est fermée.
boolean closed() :

Retourne true si le MessageQueue est fermé, sinon false.
3. Description des événements dans un environnement événementiel
3.1. AcceptListener
Lorsqu'une tâche écoute sur un port via bind(), un AcceptListener est associé à ce port. Dès qu'une connexion entrante est acceptée, le AcceptListener.accepted() est appelé, fournissant un MessageQueue prêt à envoyer et recevoir des messages.
3.2. ConnectListener
Lorsqu'une tâche tente de se connecter à un autre broker via connect(), un ConnectListener est associé à cette tentative de connexion. Si la connexion réussit, ConnectListener.connected() est appelé avec un MessageQueue. Si la connexion échoue, ConnectListener.refused() est invoqué pour signaler l'échec.
3.3. MessageQueue.Listener
Chaque MessageQueue est capable de recevoir des messages en arrière-plan. Lorsqu'un message est reçu, le MessageQueue.Listener.received() est déclenché avec le contenu du message. Si la connexion est fermée, MessageQueue.Listener.closed() est appelé.