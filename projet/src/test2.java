import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class test2 {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(12345);
        Socket s = new Socket();
        s.connect(address);
        Scanner scanner =  new Scanner(System.in);

        while (scanner.hasNextLine()) {

        }

        }
}
