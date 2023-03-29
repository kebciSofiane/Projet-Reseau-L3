import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Publisher {

        public static void main(String[] args) throws IOException {


            InetSocketAddress address = new InetSocketAddress(12345);

            Socket s = new Socket();

            s.connect(address);

            System.out.print("Welcome Publisher, choose a username :");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            RequestPublish requestPublish =new RequestPublish(s);
            requestPublish.publish(username);

        }
    }
