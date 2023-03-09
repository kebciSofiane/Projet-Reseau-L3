
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) throws IOException {

        if (args.length<2) {
            System.out.println("Saisir le serveur et le port");
        }
        Scanner scanner =  new Scanner(System.in);

        InetSocketAddress adress = new InetSocketAddress(1234);
        Socket s = new Socket();
        s.connect(adress);
        System.out.print("Welcome, choose a username :");
        String username = scanner.nextLine();
        String publish="yes";
        String message;
/*
        do {
            System.out.println("Do you want to publish your next messages ? yes/no");
            publish = scanner.nextLine();
        } while (!publish.equals("yes") && !publish.equals("no"));*/

        if (publish == "yes")
            message ="PUBLISH@"+username;
        else
            message ="@"+username;

        while(scanner.hasNextLine()){
            String textImput = scanner.nextLine()+"\n";
            message =message+"#"+textImput;
            DataOutputStream os = new DataOutputStream(s.getOutputStream());
            os.writeUTF(message);
            message="";
        }
    }
}

