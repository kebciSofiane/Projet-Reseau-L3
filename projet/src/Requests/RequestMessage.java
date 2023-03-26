package Requests;


import java.io.*;
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
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(message);
        osw.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        response = reader.readLine();
        System.out.print("The message is : ");
        System.out.println(response);
    }

    }
