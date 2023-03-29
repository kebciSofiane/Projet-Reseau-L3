import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Follower {

    public static void main(String[] args) throws IOException, SQLException {

        Scanner scanner =  new Scanner(System.in);

        InetSocketAddress address = new InetSocketAddress(12345);

        Socket s = new Socket();

        s.connect(address);


        System.out.print("Welcome, choose a username :");
        String username = scanner.nextLine();

        int request;
        int id;
        do {
            do {
                    System.out.println("What do you want to do ?");
                    System.out.println("2-Receive IDs");
                    System.out.println("3-Receive a message");
                    System.out.println("4-Answer to a message");
                    System.out.println("5-Republish a message");

                System.out.println("00-Quit");

                    request = Integer.parseInt(scanner.nextLine());

                } while (request!=2 && request!=3  && request!=4 && request!=5 && request!=00);

        switch (request) {
            case 2:
                RequestIDs requestIDs = new RequestIDs(s);
                requestIDs.requestIds();
                break;

            case 3:
                RequestMessage requestMessage = new RequestMessage(s);
                requestMessage.requestMessage();
                break;

            case 4:
                RequestReply requestReply =new RequestReply(s);
                requestReply.reply(username);
                break;

            case 5:
                scanner =  new Scanner(System.in);
                ArrayList<Integer> ids;
                do {
                    System.out.print("The republished message id :");
                    id = Integer.parseInt(scanner.nextLine());
                    DataBaseRequests dataBaseRequests = new DataBaseRequests();
                    ids =dataBaseRequests.selectDataID("Select ID from MESSAGES;",100,null);
                } while (!ids.contains(id));
                RequestRepublish requestRepublish = new RequestRepublish(s);
                requestRepublish.republish(username,id);
                break;
        }

        } while (request != 00);

    }
}
