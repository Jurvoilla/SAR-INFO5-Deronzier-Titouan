package task1;

public abstract class Broker {
    String name;
    
    // Constructeur
    Broker(String name) {
        this.name = name;
    }
    
    public String name() {
    	return this.name;
    }
    
    // Accepte une connexion sur un port donné et retourne un channel pour cette connexion
    public abstract Channel accept(int port);
    
    // Se connecte à un autre broker via un nom et un port, retourne un channel pour cette connexion
    public abstract Channel connect(String name, int port);
}