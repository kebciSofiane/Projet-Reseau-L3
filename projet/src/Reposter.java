import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reposter {
    public static void main(String[] args) throws IOException, SQLException {

        InetSocketAddress address = new InetSocketAddress("localhost",12345);

        Socket s = new Socket();

        s.connect(address);


        String userName ="The Republisher";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose some users with a blank space between each one !");
        String users =scanner.nextLine();
        String[] usersTab = users.split(" ");
        for (int i=0; i<usersTab.length;i++){
            DataBaseRequests dataBaseRequests = new DataBaseRequests();
            ArrayList<Integer> ids =dataBaseRequests.selectDataID("Select ID from MESSAGES where USERNAME='@"+usersTab[i]+"';",100,null);
            RequestRepublish requestRepublish =new RequestRepublish(s);

            for (int j=0; j<ids.size();j++)
                requestRepublish.republish(userName, ids.get(j));
        }
    }
}
