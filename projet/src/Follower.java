import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

public class Follower {

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
                    System.out.println("2-Receive IDs");
                    System.out.println("3-Receive a message");
                    System.out.println("4-Answer to a message");
                    System.out.println("5-Republish a message");
                    System.out.println("6-Connect");

                System.out.println("00-Quit");

                    request = Integer.parseInt(scanner.nextLine());

                } while (request != 1 && request!=2 && request!=3  && request!=4 && request!=5 && request!=6 && request!=00);

        switch (request) {
            case 2:
                String tag ="hello";
                String user = "sofiane";
                int limit =3;
                message = "RCV_IDS @" + user +"#"+tag +"<"+limit+"\n";
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

            case 6:
                message = "CONNECT @" + username + "\n";
                os.writeUTF(message);
                is = new DataInputStream(s.getInputStream());
                response = is.readUTF();
                System.out.println(response);
                int choice ;
                do {
                    System.out.println("Choose an option");
                    System.out.println("1-Subscribe");
                    System.out.println("2-Subscribe");
                    System.out.println();
                    System.out.println("Your feed :");
                    choice = Integer.parseInt(scanner.nextLine());
                } while (choice!=1 && choice!=2);


                switch (choice){
                    case 1 :
                        String userTagChoice;
                        String tagOrUser;
                        do {
                            System.out.println("Subscribe to a user or a tag ? user/tag");
                             userTagChoice = scanner.nextLine().toLowerCase();
                        } while (!Objects.equals(userTagChoice, "tag") && !Objects.equals(userTagChoice, "user"));
                        if (userTagChoice.equals("tag")){
                             System.out.println("Tag : ");
                             tagOrUser=scanner.nextLine().toLowerCase();
                             message = "SUBSCRIBE #" + tagOrUser + "\n";

                        }
                        else {
                            System.out.println("User : ");
                            tagOrUser=scanner.nextLine().toLowerCase();
                            message = "SUBSCRIBE @" + tagOrUser + "\n";

                        }
                        os.writeUTF(message);

                }
                is = new DataInputStream(s.getInputStream());
                response = is.readUTF();
                System.out.println(response);

                break;
        }

        } while (request != 00);

    }
}
