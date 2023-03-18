package Requests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        os.writeUTF(message);
        is = new DataInputStream(socket.getInputStream());
        String response = is.readUTF();
        System.out.println(response);
    }

    }
