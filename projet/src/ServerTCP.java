
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    // TODO: 11/03/2023
// faut pas ajouter les donner une fois arrivées dans le serveur ?

    static int id =0;
    static Map<String, ArrayList<Map<Integer, String>>> messages = new HashMap<>();


    public static int findId(){
        id =id+1;
        return id;
    }
    public static synchronized void addMessage(String message,String username) {
        ArrayList<Map<Integer, String>> listMessages= new ArrayList<>();
        if (messages.keySet().contains(username))
        {
            Map<Integer, String> messageMap = new HashMap<>();
            messageMap.put(findId(), message);
            listMessages= messages.get(username);
            listMessages.add(messageMap);
            messages.put(username,listMessages);
        } else
        {
            Map<Integer, String> messageMap = new HashMap<>();
            messageMap.put(findId(), message);
            listMessages.add(messageMap);
            messages.put(username,listMessages);
        }
    }
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

