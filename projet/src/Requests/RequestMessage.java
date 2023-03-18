package Requests;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class RequestMessage {
    DataOutputStream os;
    Socket socket;
    String message;
    DataInputStream is;


    public RequestMessage(Socket socket) throws IOException {
        this.socket = socket;
        os = new DataOutputStream(socket.getOutputStream());

    }


    public void requestMessage() throws IOException {
        Scanner scanner =  new Scanner(System.in);
        String response;
        System.out.print("The message id : ");
        String textInput = scanner.nextLine();
        message = "RCV_MSG " + textInput + "\n";
        os.writeUTF(message);
        is = new DataInputStream(socket.getInputStream());
        response = is.readUTF();
        System.out.print("The message is : ");
        System.out.println(response);
    }

    }
