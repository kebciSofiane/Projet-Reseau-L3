import Requests.RequestRepublish;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class Reposter {
    public static void main(String[] args) throws IOException, SQLException {
        if (args.length<2) System.out.println("Saisir le serveur et le port");

        InetSocketAddress address = new InetSocketAddress(12345);

        Socket s = new Socket();

        s.connect(address);


        String userName ="The Republisher";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose some users with a blank space between each one !");
        String users =scanner.nextLine();
        String[] usersTab = users.split(" ");
        for (int i=0; i<usersTab.length;i++){
            DataBaseRequests dataBaseRequests = new DataBaseRequests();
            String ids =dataBaseRequests.selectDataID("Select ID from MESSAGES where USERNAME='@"+usersTab[i]+"';",10,null);
            String[] g = ids.split("-");
            RequestRepublish requestRepublish =new RequestRepublish(s);
            for (int j=0; j<g.length;j++)
                requestRepublish.republish(userName, Integer.parseInt(g[j]));
        }
    }
}
