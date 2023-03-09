
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClientHandler extends Thread {
    private final Socket socket;
    String username = "";
    Boolean publish=false;
    Map<String, ArrayList<String>> messages = new HashMap<>();
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        PrintWriter out;
        BufferedReader in;
        try {
            out = new PrintWriter(
                    socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String line ;
            while ((line = in.readLine()) != null) {
                if (username== ""){
                    username = line.substring(line.indexOf("@"),line.indexOf("#")).trim();
                    if (line.substring(0,line.indexOf("@")).trim().equals("PUBLISH")){
                        publish=true;
                }
                }
                String message = line.substring(line.indexOf("#")+1);
                ServerTCP.addMessage(message,username);
                if (publish){
                System.out.printf(
                        "-> %s\n",
                        username+": "+message);
                out.println("-> ok");
                }
            }
            System.out.println(ServerTCP.messages);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}