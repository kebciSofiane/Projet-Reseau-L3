import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MicroblogCentralServer {
    static Map<Socket,String> sockets = new HashMap();
    static ExecutorService executor = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("A new connection identified from port : " + clientSocket.getPort());
            System.out.println("Thread assigned");
            sockets.put(clientSocket,"-");
            executor.execute(new ClientHandler(clientSocket));
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private static Map<Socket,String> allSockets = new HashMap<>();
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
            try {
                String username = null;
                DataInputStream is = new DataInputStream(clientSocket.getInputStream());

                allSockets = sockets;
                System.out.println(sockets.size());

                OutputStream outputStream = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                writer.println("Bienvenue sur le serveur de microblogs!");
                String line;

                while ((line = is.readLine()) != null) {
                    if (username == null) {
                        username = line.substring(line.indexOf("@")).trim();
                        sockets.replace(clientSocket, "-", username);

                    }
                    else{
                        username = line.substring(line.indexOf("@"), line.indexOf("#")).trim();
                    String message = line.substring(line.indexOf("#") + 1).trim();
                    System.out.println("Received data from " +username+": " + message);
                    for (Map.Entry mapentry : sockets.entrySet()) {
                        Socket socket = (Socket) mapentry.getKey();
                        if (socket != clientSocket) {
                            DataBaseRequests dataBaseRequests = new DataBaseRequests();
                            ArrayList<String> tagsList = dataBaseRequests.selectDataTags
                                    ("Select TAG from TAGS where USERNAME='" + mapentry.getValue() + "';");
                            ArrayList<String> favoriteUsersList = dataBaseRequests.selectDataUsernames
                                    ("Select USER from USERS where USERNAME='" + mapentry.getValue() + "';");

                            for (String tag : tagsList)
                                if (message.contains(tag)) {
                                    OutputStream out = socket.getOutputStream();
                                    PrintWriter w = new PrintWriter(out, true);
                                    w.println(username + ": " + message);
                                    break;
                                }
                            for (String user : favoriteUsersList)
                                if (username.equals(user)) {
                                    OutputStream out = socket.getOutputStream();
                                    PrintWriter w = new PrintWriter(out, true);
                                    w.println(username + ": " + message);
                                    break;
                                }
                        }
                    }
                }
                    }

                    is.close();
                    sockets.remove(clientSocket);
                    clientSocket.close();
                } catch(IOException e){
                    e.printStackTrace();

                } catch(SQLException e){
                    throw new RuntimeException(e);
                }
            }
    }
}