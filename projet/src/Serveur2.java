import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Serveur2 {
    public static void main(String[] args) {
        try {
            // Création d'un socket pour écouter les demandes de connexion du serveur 1
            ServerSocket serverSocket = new ServerSocket(8081);
            System.out.println("Serveur 2 en attente de connexion...");


            Socket clientSocket;
            clientSocket = serverSocket.accept();
            System.out.println("Connexion reçue !");

            // Attente d'une demande de connexion
            InetSocketAddress adress = new InetSocketAddress(8080);
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
