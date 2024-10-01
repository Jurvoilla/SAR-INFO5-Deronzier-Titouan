package task1;

public abstract class Channel {
    // Lit des octets à partir du canal
    public abstract int read(byte[] bytes, int offset, int length);
    
    // Écrit des octets dans le canal
    public abstract int write(byte[] bytes, int offset, int length);
    
    // Déconnecte le canal
    public abstract void disconnect();
    
    // Vérifie si le canal est déconnecté
    abstract boolean disconnected();
}
