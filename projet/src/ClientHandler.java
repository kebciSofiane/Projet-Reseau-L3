import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;


public class ClientHandler extends Thread {
    private final Socket socket;
    String username = "";

    String set;
    Boolean[] requests=new Boolean[10];

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        OutputStream out;

        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());

            String line ;
            while ((line = is.readUTF()) != null){
                String request;
                for (int i=0 ; i<9;i++) requests[i] =false;

                try {
                    request =line.substring(0,line.indexOf(" ")).trim();
                }
                catch (StringIndexOutOfBoundsException e)  {
                    request="ERROR";
                }
                switch (request){
                    case "PUBLISH" : requests[0] =true;
                    case "ERROR" : requests[0] =true;
                    case "RCV_IDS" : requests[1] =true;
                    case "RCV_MSG" : requests[2] =true;
                    case "REPLY" : requests[3] =true;
                    case "REPUBLISH" : requests[4] =true;
                    case "CONNECT" : requests[5] =true;
                    case "SUBSCRIBE" : requests[6] =true;

                }


                if (requests[0]){
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    if (this.username == "") this.username = line.substring(line.indexOf("@"), line.indexOf("#")).trim();
                    String message = line.substring(line.indexOf("#")+1);
                    dataBaseRequests.updateData("Insert into MESSAGES values("+
                            dataBaseRequests.findId()+",'"+ this.username +"','"+message+"');");
                    System.out.printf(
                            "-> %s\n",
                            this.username +": "+message);
                    os.writeUTF("Message sent successfully");
                    dataBaseRequests.closeBD();

                }

                else if (requests[1]) {
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    set = dataBaseRequests.selectDataID("Select* from MESSAGES ORDER BY id DESC limit 5 ;");
                    os.writeUTF(set);
                    dataBaseRequests.closeBD();

                }
                else if (requests[2]) {
                    String id = line.substring(line.indexOf(" ")+1);
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    set = dataBaseRequests.selectDataMessage("Select MESSAGE from MESSAGES where ID ='"+id+"';");
                    os.writeUTF(set);
                    dataBaseRequests.closeBD();
                }

                else if (requests[3]) {
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    if (this.username == "") this.username = line.substring(line.indexOf("@"), line.indexOf("*")).trim();
                    String message = line.substring(line.indexOf("#")+1);
                    String id = line.substring(line.indexOf("*")+1,line.indexOf("#"));
                    set = dataBaseRequests.selectDataMessage("Select MESSAGE from MESSAGES where ID ='"+id+"';");
                    if (!set.isEmpty()){
                    dataBaseRequests.updateData("Insert into MESSAGES values("+
                            dataBaseRequests.findId()+",'"+ this.username +"','"+message+"');");
                    System.out.printf("Replying to : "+ set);
                    System.out.printf(
                            "-> %s\n",
                            this.username +": "+message);
                    os.writeUTF("Message sent successfully");
                    }
                    else os.writeUTF("ERROR : Wrong id");
                    dataBaseRequests.closeBD();
                }

                else if (requests[4]) {
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    if (this.username == "") this.username = line.substring(line.indexOf("@"), line.indexOf("*")).trim();
                    String id = line.substring(line.indexOf("*")+1);
                    set = dataBaseRequests.selectDataMessage("Select MESSAGE from MESSAGES where ID ='"+id+"';");
                    if (!set.isEmpty()){
                        dataBaseRequests.updateData("Insert into MESSAGES values("+
                                dataBaseRequests.findId()+",'"+ this.username +"','"+set+"');");
                        System.out.print("Republishing :");
                        System.out.printf(
                                "-> %s\n",
                                this.username +": "+set);
                        os.writeUTF("Message republished successfully");
                    }
                    else os.writeUTF("ERROR : Wrong id");
                    dataBaseRequests.closeBD();
                }

                else if (requests[5]){
                    if (this.username == "") this.username = line.substring(line.indexOf("@")).trim();
                    os.writeUTF("Welcome back "+username);
                }
                else if (requests[6]){
                    if (this.username == "") this.username = line.substring(line.indexOf("@")).trim();
                    os.writeUTF("Welcome back "+username);
                }



            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}