Il faut des channel entre des tache. C'est les broker qui créer les tâches.
Tache communique avec des broker.
1 tache connait donc au moin un broker.
Lorsqu'on connect, on s'attend a ce que qqun accepte.
Une tache peut parler à plusieur broker.

Kel e la propriete du channel? Il faut décider des propriété du channel.
Est-ce kil va perdre des octets ?
Est ce que l'ordre va changer ?
Il est FIFO-lossless. Flux bidirectionnel ou unidirectionnel ? On veut du full duplex.

TCP flux d'octet -> je pousse -> pa de correlation entre ce ke je pousse et ce que je recoit
Tant que g pa reçu ce que j'attend.
UDP flu de paquet -> il envoie le paket ou il le perd. Soit les 25 arive soit ils arrive pa.

Comment les tache ont le droit d'utiliser les channels (parralélisme ou pa)?

En flu le multi-thread dan channel pa pareil qu'en paquet!
Channel e pa multithreader
Le channel n'est pa posséder de manière unique par un thread, mais n'est pas thread safe.
-------------
Class Broker:
-------------

Gère et met en place les connections des cannaux.
Un broker par machine identifier par un nom.
Pour établir une connexion, le broker n°1 appelle connect et le broker n°2
doit accepter la connexion.
Il peut y avoir plusieurs tâche dans un meme broker.
2 tâches ne peuvent pas accepter sur un même port d'un même broker.

--------------------------------------------------------------
Broker(String name) : constructeur

Son nom permet d'identifier la tâche lors de la connexion. 
Le nom est unique.
--------------------------------------------------------------
Channel accept(int port)

port : le port d'écoute à ouvrir. La notion de port est unique par nom de broker.
Retourne le channel qui lie les ports ouverts des 2 tâches.
Si il n'y a aucune demande sur le port, ne fait rien.
--------------------------------------------------------------
Channel connect(String name, int port)

name: le nom du broker auquel on veut se connecter
port: le port de la tâche courante qu'on ouvre pour la connexion
La fonction est blocante tant qu'aucune que la connexion n'est pas accepté par
le serveur cible.
--------------------------------------------------------------

--------------
Class Channel:
--------------

Permet de gérer l'échange de message dans entre les tasks.
Il y a seul channel entre deux port d'un même nom de broker.

--------------------------------------------------------------
int read(byte[] bytes, int offset, int length)

bytes: le tableau de bits dans lequel mettre ce qu'on a lu. 
Si le tableau n'est pas vide, remplie à partir du dernier bit remplie.
Si on dépasse le tableau, retourne une exception.
offset: Doit être >= 0.
length: le nombre de bits maximum à lire. Doit être >= 0.

Retourne le nombre de bits réellement lu du channel.
--------------------------------------------------------------
int write(byte[] bytes, int offset, int length)

bytes: le tableau des bits qui contient ce qu'on veut envoyer dans le channel.
offset: L'indice a partir duquel envoyer les bits dans le tableau bytes. Doit être >= 0.
length: le nombre maximum de bit a envoyer. Doit être >= 0.

Retourne le nombre de bit réellement envoyé.
--------------------------------------------------------------
void disconnect()

Déconnecte le channel. Si le channel est déjà déconnecté, ne fait rien.
--------------------------------------------------------------
boolean disconnected()

Retourne si le channel est actullement connecté ou non.
--------------------------------------------------------------

-----------
Class Task:
-----------
C'est la classe qui s'occupe des calculs et des traitements.

--------------------------------------------------------------
Task(Broker b, Runnable r)
Permet de lancer la tache dans un runnable.
--------------------------------------------------------------
static Broker getBroker()
Retourne un Broken alloué.
--------------------------------------------------------------