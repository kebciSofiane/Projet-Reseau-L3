package Requests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class RequestReply {
    DataOutputStream os;
    Socket socket;
    String message;
    DataInputStream is;

    public RequestReply(Socket socket) throws IOException {
        this.socket = socket;
        os = new DataOutputStream(socket.getOutputStream());

    }

    public void reply(String username) throws IOException {
        Scanner scanner =  new Scanner(System.in);

        System.out.print("Your reply id :");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Your reply message :");
        String messageReply = scanner.nextLine();
        message = "REPLY @" + username + "*" + id + "#" + messageReply + "\n";
        os.writeUTF(message);
        is = new DataInputStream(socket.getInputStream());
        String response = is.readUTF();
        System.out.println(response);
    }

    }



