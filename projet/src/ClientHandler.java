import java.io.*;
import java.net.Socket;
import java.sql.SQLException;


public class ClientHandler extends Thread {
    private final Socket socket;
    String username = "";

    String set;
    Boolean[] requests=new Boolean[3];

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        OutputStream out;
        requests[0] =false;
        requests[1] =false;
        requests[2] =false;

        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());

            String line ;

            while ((line = is.readUTF()) != null){
                String request;
                requests[0] =false;
                requests[1] =false;
                requests[2] =false;

                try {
                    request =line.substring(0,line.indexOf(" ")).trim();
                } catch (StringIndexOutOfBoundsException e)  {
                    request="ERROR";
                }
                if (request.equals("PUBLISH") || request.equals("ERROR")) requests[0] =true;
                else if (request.equals("RCV_IDS")) requests[1]=true;
                else if (request.equals("RCV_MSG"))requests[2]=true;



                if (requests[0]){
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    if (username== "") username = line.substring(line.indexOf("@"),line.indexOf("#")).trim();
                    String message = line.substring(line.indexOf("#")+1);
                    //DataBaseRequests.updateData("INSERT INTO MESSAGES VALUES(2,'@Mériem','hiifazzzzzzvgiii')");
                    dataBaseRequests.updateData("Insert into MESSAGES values("+
                            dataBaseRequests.findId()+",'"+username+"','"+message+"');");
                    System.out.printf(
                            "-> %s\n",
                            username+": "+message);
                    os.writeUTF("Message sent successfully");
                }
                else if (requests[1]) {
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                     set = dataBaseRequests.selectDataID("Select* from MESSAGES ORDER BY id DESC limit 5 ;");
                     os.writeUTF(set);
                }
                else if (requests[2]) {
                    System.out.println("ggggg");
                    String id = line.substring(line.indexOf(" ")+1);
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    set = dataBaseRequests.selectDataMessage("Select MESSAGE from MESSAGES where ID ='"+id+"';");
                    os.writeUTF(set);
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}