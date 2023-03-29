import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestReply {
    Socket socket;
    String message;

    public RequestReply(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void reply(String username) throws IOException, SQLException {
        Scanner scanner =  new Scanner(System.in);

        ArrayList<Integer> ids;
        int id;
        do {
            System.out.print("Your reply id :");
            id = Integer.parseInt(scanner.nextLine());
            DataBaseRequests dataBaseRequests = new DataBaseRequests();
            ids =dataBaseRequests.selectDataID("Select ID from MESSAGES;",100,null);
        } while (!ids.contains(id));

        System.out.print("Your reply message :");
        String messageReply = scanner.nextLine();
        message = "REPLY @" + username + "*" + id + "#" + messageReply + "\n";
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(message);
        osw.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(reader.readLine());
    }

    }



