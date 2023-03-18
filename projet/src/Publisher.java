import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Publisher {

        public static void main(String[] args) throws IOException {
            if (args.length < 2) System.out.println("Saisir le serveur et le port");

            Scanner scanner = new Scanner(System.in);

            InetSocketAddress address = new InetSocketAddress(12345);

            Socket s = new Socket();

            s.connect(address);

            DataOutputStream os = new DataOutputStream(s.getOutputStream());


            System.out.print("Welcome Publisher, choose a username :");
            String username = scanner.nextLine();
            String response;
            DataInputStream is;
            String message;
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
        }
    }
