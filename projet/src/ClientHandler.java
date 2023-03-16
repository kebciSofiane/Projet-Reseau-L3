import java.io.*;
import java.net.Socket;
import java.sql.SQLException;


public class ClientHandler extends Thread {
    private final Socket socket;
    String username = "";

    String set;
    Boolean[] requests=new Boolean[6];

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
                requests[0] =false;
                requests[1] =false;
                requests[2] =false;
                requests[3] =false;
                requests[4] =false;



                try {
                    request =line.substring(0,line.indexOf(" ")).trim();
                } catch (StringIndexOutOfBoundsException e)  {
                    request="ERROR";
                }
                if (request.equals("PUBLISH") || request.equals("ERROR")) requests[0] =true;
                else if (request.equals("RCV_IDS")) requests[1]=true;
                else if (request.equals("RCV_MSG"))requests[2]=true;
                else if (request.equals("REPLY"))requests[3]=true;
                else if (request.equals("REPUBLISH"))requests[4]=true;



                if (requests[0]){
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    if (this.username == "") this.username = line.substring(line.indexOf("@"), line.indexOf("#")).trim();
                    String message = line.substring(line.indexOf("#")+1);
                    //DataBaseRequests.updateData("INSERT INTO MESSAGES VALUES(2,'@Mériem','hiifazzzzzzvgiii')");
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
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}