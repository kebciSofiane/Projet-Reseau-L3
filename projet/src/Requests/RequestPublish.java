package Requests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class RequestPublish {

    DataOutputStream os;
    Socket socket;
    String message;
    DataInputStream is;

    public RequestPublish(Socket socket) throws IOException {
        this.socket = socket;
        os = new DataOutputStream(socket.getOutputStream());

    }


    public void publish(String username) throws IOException {
        Scanner scanner =  new Scanner(System.in);
        message = "PUBLISH @" + username;
        System.out.println("Your messages : ");
        while (scanner.hasNextLine()) {
            String textInput = scanner.nextLine();
            message = message + "#" + textInput + "\n";
            System.out.println(message);
            os.writeUTF(message);
            message = "PUBLISH ";
            is = new DataInputStream(socket.getInputStream());
            String response = is.readUTF();
            System.out.println(response);
        }

    }

    }
