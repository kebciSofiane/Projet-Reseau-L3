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
        ServerSocket serverSocket = new ServerSocket(12345);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("A new connection identified from port : " + clientSocket.getPort());
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println("Bienvenue sur le serveur de microblogs!");
            writer.flush();
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
            PrintWriter w = null;
            try {
                String username = null;
                DataInputStream is = new DataInputStream(clientSocket.getInputStream());
                OutputStream out = clientSocket.getOutputStream();
                w = new PrintWriter(out, true);

                DataBaseRequests dataBaseRequests = new DataBaseRequests();
                allSockets = sockets;

                String line;

                while ((line = is.readLine()) != null) {
                    if (username == null) {
                        username = line.substring(line.indexOf("@")).trim();
                        sockets.replace(clientSocket, "-", username);
                    }
                    else{
                        String request =line.substring(0,line.indexOf(" ")).trim();
                        String message = null;
                        int idRcv;

                        switch (request) {
                            case "REPLY" -> {
                                //username = line.substring(line.indexOf("@"), line.indexOf("#")).trim();
                                username = line.substring(line.indexOf("@"), line.indexOf("*"));
                                idRcv = Integer.parseInt(line.substring(line.indexOf("*") + 1, line.indexOf("#")));
                                ArrayList<String> replyingMessage =
                                        dataBaseRequests.selectData("Select MESSAGE FROM MESSAGES where ID=" + idRcv + ";","MESSAGE");
                                ArrayList<String> replyingUser =
                                        dataBaseRequests.selectData("Select USERNAME FROM MESSAGES where ID=" + idRcv + ";","USERNAME");

                                System.out.println("Replying to "+replyingUser.get(0)+"'s message : " + replyingMessage.get(0));
                                message = line.substring(line.indexOf("#") + 1).trim();
                            }
                            case "REPUBLISH" -> {
                                username = line.substring(line.indexOf("@"), line.indexOf("*"));
                                idRcv = Integer.parseInt(line.substring(line.indexOf("*") + 1));
                                ArrayList<String> republishingUser =
                                        dataBaseRequests.selectData("Select USERNAME FROM MESSAGES where ID=" + idRcv + ";","USERNAME");
                                System.out.println("Republishing "+republishingUser.get(0)+"'s message : "   );
                                message =
                                        dataBaseRequests.selectData("Select MESSAGE FROM MESSAGES where ID=" + idRcv + ";","MESSAGE").get(0);
                            }
                            case "PUBLISH" -> message = line.substring(line.indexOf("#") + 1).trim();
                        }
                        int id = dataBaseRequests.findId();
                        System.out.println("-->" +username+": id ="+id+" : " + message);

                        dataBaseRequests.updateData("Insert into MESSAGES values("+id
                                +",'"+ username +"','"+message+"');");

                        OutputStreamWriter osw = new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
                        osw.write("-Sent-\n");
                        osw.flush();
                        for (Map.Entry mapentry : sockets.entrySet()) {
                            Socket socket = (Socket) mapentry.getKey();
                            if (socket != clientSocket) {
                                ArrayList<String> tagsList = dataBaseRequests.selectData
                                        ("Select TAG from TAGS where USERNAME='" + mapentry.getValue() + "';","TAG");
                                ArrayList<String> favoriteUsersList = dataBaseRequests.selectData
                                        ("Select USER from USERS where USERNAME='" + mapentry.getValue() + "';","USER");

                                for (String tag : tagsList) {
                                    assert message != null;
                                    if (message.contains(tag)) {
                                         out = socket.getOutputStream();
                                         w = new PrintWriter(out, true);
                                        w.println(username + ": " + message);
                                        break;
                                    }
                                }
                                for (String user : favoriteUsersList)
                                    if (username.equals(user)) {
                                         out = socket.getOutputStream();
                                         w = new PrintWriter(out, true);
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
                System.out.println("ERROR Server");
            } catch(SQLException e){
                System.out.println("ERROR database");
            }

            finally {
                w.println("ERROR");
            }
        }
    }
}