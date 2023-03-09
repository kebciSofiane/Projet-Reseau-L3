
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
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

