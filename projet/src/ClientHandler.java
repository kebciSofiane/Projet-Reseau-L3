
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientHandler extends Thread {
    private final Socket socket;
    String username = "";
    Boolean publish=false;

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
                    System.out.println(line.substring(0,line.indexOf("@")).trim());
                    if (line.substring(0,line.indexOf("@")).trim().equals("PUBLISH")){
                        publish=true;
                }
                }

                if (publish)
                System.out.printf(
                        "-> %s\n",
                        username+": "+line.substring(line.indexOf("#")+1));
                //out.println(username+": "+line.substring(2));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}