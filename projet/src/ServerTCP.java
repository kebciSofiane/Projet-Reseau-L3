
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    // TODO: 11/03/2023
    // faut pas ajouter les donner une fois arrivées dans le serveur ?
    // The database doesn't save the ' character

    public static void main(String[] args) throws IOException
    {
        ServerSocket myserverSocket = new ServerSocket(12345);
        ExecutorService executor = Executors.newCachedThreadPool();

        while (true)
        {
            Socket mynewSocket = null;
            try
            {
                mynewSocket = myserverSocket.accept();
                //System.out.println("A new connection from : " + mynewSocket.getLocalPort());
                Thread myThread = new ClientHandler(mynewSocket);
                executor.submit(myThread);
                myThread.start();
            }
            catch (Exception e){
                mynewSocket.close();
                e.printStackTrace();
            }
        }
    }

        }

