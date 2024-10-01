Spécification supplémentaire
QueueBroker :

Le QueueBroker permet de gérer les connexions via des MessageQueue pour envoyer et recevoir des messages complets.
Chaque connexion retourne un MessageQueue permettant d'envoyer ou recevoir des messages.
MessageQueue :

Le MessageQueue permet d'envoyer et de recevoir des messages sous forme de tableaux d'octets.
Les méthodes send et receive fonctionnent au niveau des messages complets, contrairement à write et read des canaux qui sont orientées octets.
Task :

Chaque tâche peut soit utiliser un Broker pour des connexions de type flux d'octets, soit un QueueBroker pour des connexions orientées messages.
Plan d'implémentation
Nous allons implémenter les classes suivantes :

QueueBroker : gère la création de MessageQueue pour envoyer et recevoir des messages entre tâches.
MessageQueue : fournit les méthodes send et receive pour échanger des messages complets.
Task : doit supporter à la fois les connexions par Broker (flux d'octets) et par QueueBroker (messages complets).