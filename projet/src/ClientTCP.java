
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
        System.out.print("Hello,Choose a username :");
        String username = scanner.nextLine();
        String message ="@"+username;

        while(scanner.hasNextLine()){
            String textImput = scanner.nextLine()+"\n";
            message =message+"#"+textImput;
            DataOutputStream os = new DataOutputStream(s.getOutputStream());
            os.writeUTF(message);
            message="";
        }
    }
}

