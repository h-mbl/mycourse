package fx.mycourse.server;

public class ServerLauncher {

    /**
     * Le port utilisé pour le serveur
     */
    public final static int PORT = 1337;

    /**
     * Démarre le serveur sur un port donné et l'exécute.
     * @param arg un tableau d'arguments de ligne de commande (non-utilisé)
     */
    public static void main(String[] arg) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}