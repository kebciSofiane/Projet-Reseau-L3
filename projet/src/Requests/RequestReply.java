package Requests;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class RequestReply {
    Socket socket;
    String message;

    public RequestReply(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void reply(String username) throws IOException {
        Scanner scanner =  new Scanner(System.in);

        System.out.print("Your reply id :");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Your reply message :");
        String messageReply = scanner.nextLine();
        message = "REPLY @" + username + "*" + id + "#" + messageReply + "\n";
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(message);
        osw.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(reader.readLine());
    }

    }



