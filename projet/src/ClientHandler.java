
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientHandler extends Thread {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {

        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(
                    socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            String line = in.readLine() ;
            String username = line.substring(0,line.indexOf("#")).trim() ;
            System.out.println("You'll receive a message from "+username);
            do {
                System.out.printf(
                        "<- %s\n",
                        line.substring(line.indexOf("#")+1));
                out.println(line.substring(2));
            }while ((line = in.readLine()) != null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}