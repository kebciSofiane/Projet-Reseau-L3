import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) throws IOException {

        if (args.length<2) {
            System.out.println("Saisir le serveur et le port");
        }
        Scanner scanner =  new Scanner(System.in);

        InetSocketAddress adress = new InetSocketAddress(12345);
        Socket s = new Socket();
        s.connect(adress);
        System.out.print("Welcome, choose a username :");
        String username = scanner.nextLine();

        int request;
        String message;


        do {
            System.out.println("What do you want to do ?");
            System.out.println("1-PUBLISH");
            System.out.println("2-Receive IDs");
            request = Integer.parseInt(scanner.nextLine());
            System.out.println(request);
        } while (request != 1 && request!=2);

        DataOutputStream os = new DataOutputStream(s.getOutputStream());
        if (request==1){
            message ="PUBLISH@"+username;
            System.out.println("Your messages : ");
            while(scanner.hasNextLine()){
                String textInput = scanner.nextLine();
                message =message+"#"+textInput+"\n";
                os.writeUTF(message);
                message="";
                DataInputStream is = new DataInputStream(s.getInputStream());
                String response = is.readUTF();
                System.out.println(response);
            }
        }
        else if (request==2){
                message = "RCV_IDS@" + username+"\n";
                os.writeUTF(message);
                DataInputStream is = new DataInputStream(s.getInputStream());
                String response = is.readUTF();
                System.out.println("Here is some IDs");
                String[] g = response.split("-");
                for (int i =1; i<g.length;i++)
                   System.out.println(g[i]);
            }
    }
}
