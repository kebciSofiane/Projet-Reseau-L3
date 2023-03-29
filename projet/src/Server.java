
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static ExecutorService executor = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("A new connection identified from port : " + clientSocket.getPort());
            executor.execute(new ClientHandler(clientSocket));
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
            try {
                String request;
                ArrayList<Integer> ids;
                Boolean[] requests=new Boolean[10];
                for (int i=0 ; i<9;i++) requests[i] =false;
                OutputStream out = clientSocket.getOutputStream();
                PrintWriter w = new PrintWriter(out, true);


                String username = null;
                DataInputStream is = new DataInputStream(clientSocket.getInputStream());
                DataBaseRequests dataBaseRequests = new DataBaseRequests();

                String line;
                while ((line = is.readLine()) != null) {

                    for (int i=0 ; i<5;i++) requests[i] =false;

                    try {
                        request =line.substring(0,line.indexOf(" ")).trim();
                    }
                    catch (StringIndexOutOfBoundsException e)  {
                        request="ERROR";
                    }
                    switch (request){
                        case "PUBLISH"  :
                            requests[0] =true;
                            break;
                        case "ERROR" :
                            requests[0] =true;
                            break;
                        case "RCV_IDS" :
                            requests[1] =true;
                            break;
                        case "RCV_MSG" :
                            requests[2] =true;
                            break;
                        case "REPLY" :
                            requests[3] =true;
                            break;
                        case "REPUBLISH" :
                            requests[4] =true;
                            break;

                    }

                    if (requests[0]){
                        if (username == null) username = line.substring(line.indexOf("@"), line.indexOf("#")).trim();
                        String message = line.substring(line.indexOf("#")+1);
                        dataBaseRequests.updateData("Insert into MESSAGES values("+
                                dataBaseRequests.findId()+",'"+ username +"','"+message+"');");
                        System.out.printf(
                                "-> %s\n",
                                username +": "+message);
                        w.println("--Sent--");

                    }

                    else if (requests[1]) {
                        String tag=null;
                        String user=null;
                        try {
                            tag = line.substring(line.indexOf("#"));
                        }
                        catch (StringIndexOutOfBoundsException ignored)  {
                        }

                        try {
                            user = line.substring(line.indexOf("@"),line.indexOf("#"));
                        }
                        catch (StringIndexOutOfBoundsException e)  {
                            try {
                                user = line.substring(line.indexOf("@"));
                            }
                            catch (StringIndexOutOfBoundsException ignored){
                            }
                        }
                        System.out.println("tag "+tag);
                        int limit = Integer.parseInt(line.substring(line.indexOf("<")+1,line.indexOf(">")).trim());
                        if (user != null)
                            ids = dataBaseRequests.selectDataID
                                    ("Select* from MESSAGES where USERNAME= '"+user+"' ORDER BY id DESC;",limit,tag);
                        else {
                            ids = dataBaseRequests.selectDataID("Select* from MESSAGES ORDER BY id DESC;",limit,tag);

                        }

                        w.println(ids);



                    }
                    else if (requests[2]) {
                        String id = line.substring(line.indexOf(" ")+1);
                        String message = null;
                        ArrayList<String> messageList= dataBaseRequests.selectData("Select MESSAGE FROM MESSAGES where ID=" + id + ";","MESSAGE");
                        if (!messageList.isEmpty()) message = messageList.get(0);

                        w.println(message);
                    }

                    else if (requests[3]) {
                        if (username == null) username = line.substring(line.indexOf("@"), line.indexOf("*")).trim();
                        String message = line.substring(line.indexOf("#")+1);
                        String id = line.substring(line.indexOf("*")+1,line.indexOf("#"));
                        ArrayList<String> messageList= dataBaseRequests.selectData("Select MESSAGE FROM MESSAGES where ID=" + id + ";","MESSAGE");
                        String replyMessage = null;
                        String replyUser =
                                dataBaseRequests.selectData("Select USERNAME FROM MESSAGES where ID=" + id + ";","USERNAME").get(0);

                        if (!messageList.isEmpty()) replyMessage = messageList.get(0);

                        if (replyMessage!=null){
                            dataBaseRequests.updateData("Insert into MESSAGES values("+
                                    dataBaseRequests.findId()+",'"+ username +"','"+message+"');");
                            System.out.println("Replying to "+replyUser+"'s message : " + replyMessage);
                            System.out.printf(
                                    "-> %s\n",
                                    username +": "+message);
                            w.println("--Sent--");
                        }
                        else
                            w.println("ERROR : Wrong id");
                    }

                    else if (requests[4]) {
                        if (username == null) username = line.substring(line.indexOf("@"), line.indexOf("*")).trim();
                        String id = line.substring(line.indexOf("*")+1);
                        ArrayList<String> messageList= dataBaseRequests.selectData("Select MESSAGE FROM MESSAGES where ID=" + id + ";","MESSAGE");
                        String republishMessage = null;
                        String republishingUser =
                                dataBaseRequests.selectData("Select USERNAME FROM MESSAGES where ID=" + id + ";","USERNAME").get(0);
                        if (!messageList.isEmpty()) republishMessage = messageList.get(0);
                        if (republishMessage!=null){
                            dataBaseRequests.updateData("Insert into MESSAGES values("+
                                    dataBaseRequests.findId()+",'"+ username +"','"+republishMessage+"');");

                            System.out.println("Republishing "+republishingUser+"'s message : "   );
                            System.out.printf(
                                    "-> %s\n",
                                    username +": "+republishMessage);
                            w.println("--republished--");
                        }
                        else w.println("ERROR : Wrong id");

                    }

                }

                is.close();
                clientSocket.close();
                dataBaseRequests.closeBD();

            } catch(IOException e){
                e.printStackTrace();

            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}
