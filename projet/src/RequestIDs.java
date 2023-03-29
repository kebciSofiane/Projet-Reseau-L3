import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class RequestIDs {
     Socket socket;
     String message;


    public RequestIDs(Socket socket) throws IOException {
        this.socket = socket;

    }

    public void requestIds() throws IOException {
        Scanner scanner =  new Scanner(System.in);

        String tags ;
        String user ;
        String response = null;
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
            message = message+"#"+tags;

        }
        message+="\n";
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(message);
        osw.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        response =reader.readLine();

        try {
            System.out.print("Here is some IDs : "+response);
        }
        catch (ArrayIndexOutOfBoundsException e)  {
            System.out.println("No IDs found :(");
        }

    }
}
