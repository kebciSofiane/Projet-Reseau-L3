package Requests;

import java.io.*;
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
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            osw.write(message);
            osw.flush();
            message = "PUBLISH ";
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(reader.readLine());


        }


    }

    }
