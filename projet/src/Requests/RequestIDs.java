package Requests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class RequestIDs {
     DataOutputStream os;
     Socket socket;
     String message;
     DataInputStream is;


    public RequestIDs(Socket socket) throws IOException {
        this.socket = socket;
        os = new DataOutputStream(socket.getOutputStream());

    }


    public void requestIds() throws IOException {
        Scanner scanner =  new Scanner(System.in);

        String tags ;
        String user ;
        String response;
        int limit;
        String rep;
        System.out.print("Number of IDS : ");
        limit = Math.abs(Integer.parseInt(scanner.nextLine()));

        message = "RCV_IDS <"+limit+">";

        do {
            System.out.println("Do you want to specify a username ? y/n");
            rep = scanner.nextLine();
        } while (!Objects.equals(rep, "y") && !Objects.equals(rep, "n"));
        if (rep.equals("y")){
            System.out.print("Username : ");
            user = scanner.nextLine();
            message = message+"@" + user;

        }
        do {
            System.out.println("Do you want to specify a tag ? y/n");
            rep = scanner.nextLine();
        } while (!Objects.equals(rep, "y") && !Objects.equals(rep, "n"));
        if (rep.equals("y")){
            System.out.print("Tags separated by a blank space : ");
            tags = scanner.nextLine();
            message = message+"#"+tags+"\n";

        }
        os.writeUTF(message);
        is = new DataInputStream(socket.getInputStream());
        response = is.readUTF();

        try {
            System.out.print("Here is some IDs : ");
            String[] ids = response.split("-");
            System.out.print("[" + ids[1]);
            for (int i = 2; i < ids.length; i++)
                System.out.print("," + ids[i]);
            System.out.println("]");
            System.out.println();
        }
        catch (ArrayIndexOutOfBoundsException e)  {
            System.out.println("No IDs found :(");
        }

    }
}
