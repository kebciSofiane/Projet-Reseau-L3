import java.net.*;
import java.io.*;

public class Serveur1 {
    public static void main(String[] args) {
        try {
            // Création d'un socket pour écouter les connexions entrantes
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Serveur 1 en attente de connexion...");

            Socket clientSocket;

            clientSocket = serverSocket.accept();
            System.out.println("Connexion reçue !");

            InetSocketAddress adress = new InetSocketAddress(8081);
            Socket s = new Socket();
            s.connect(adress);

            // Fermeture des sockets
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
