import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class RequestRepublish {

    DataOutputStream os;
    Socket socket;
    String message;
    DataInputStream is;

    public RequestRepublish(Socket socket) throws IOException {
        this.socket = socket;
        os = new DataOutputStream(socket.getOutputStream());

    }


    public void republish(String username, int id) throws IOException {
        message = "REPUBLISH @" + username + "*" + id + "\n";
        System.out.println(message);

        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(message);
        osw.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(reader.readLine());
    }

    }
