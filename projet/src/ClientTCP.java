import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) throws IOException {
        if (args.length<2) System.out.println("Saisir le serveur et le port");

        Scanner scanner =  new Scanner(System.in);

        InetSocketAddress address = new InetSocketAddress(12345);

        Socket s = new Socket();

        s.connect(address);

        DataOutputStream os = new DataOutputStream(s.getOutputStream());


        System.out.print("Welcome, choose a username :");
        String username = scanner.nextLine();

        int request;
        String response;
        int id;
        DataInputStream is;
        do {
            String message;
            do {
                    System.out.println("What do you want to do ?");
                    System.out.println("1-Publish a message");
                    System.out.println("2-Receive IDs");
                    System.out.println("3-Receive a message");
                    System.out.println("4-Answer to a message");
                    System.out.println("5-Republish a message");
                    System.out.println("00-Quit");

                    request = Integer.parseInt(scanner.nextLine());

                } while (request != 1 && request!=2 && request!=3  && request!=4 && request!=5 && request!=00);

        switch (request) {
            case 1:
                message = "PUBLISH @" + username;
                System.out.println("Your messages : ");
                while (scanner.hasNextLine()) {
                    String textInput = scanner.nextLine();
                    message = message + "#" + textInput + "\n";
                    os.writeUTF(message);
                    message = "";
                    is = new DataInputStream(s.getInputStream());
                    response = is.readUTF();
                    System.out.println(response);
                }
                break;
            case 2:
                message = "RCV_IDS @" + username + "\n";
                os.writeUTF(message);
                is = new DataInputStream(s.getInputStream());
                response = is.readUTF();
                System.out.print("Here is some IDs : ");
                String[] g = response.split("-");
                System.out.print("[" + g[1]);
                for (int i = 2; i < g.length; i++)
                    System.out.print("," + g[i]);
                System.out.println("]");
                System.out.println();
                break;

            case 3:
                System.out.print("The message id : ");
                String textInput = scanner.nextLine();
                message = "RCV_MSG " + textInput + "\n";
                os.writeUTF(message);
                is = new DataInputStream(s.getInputStream());
                response = is.readUTF();
                System.out.print("The message is : ");
                System.out.println(response);
                break;

            case 4:
                System.out.print("Your reply id :");
                id = Integer.parseInt(scanner.nextLine());
                System.out.print("Your reply message :");
                String messageReply = scanner.nextLine();
                message = "REPLY @" + username + "*" + id + "#" + messageReply + "\n";
                os.writeUTF(message);
                is = new DataInputStream(s.getInputStream());
                response = is.readUTF();
                System.out.println(response);
                break;

            case 5:
                System.out.print("The republished message id :");
                id = Integer.parseInt(scanner.nextLine());
                message = "REPUBLISH @" + username + "*" + id + "\n";
                os.writeUTF(message);
                is = new DataInputStream(s.getInputStream());
                response = is.readUTF();
                System.out.println(response);
                break;
        }

        } while (request != 00);

    }
}
