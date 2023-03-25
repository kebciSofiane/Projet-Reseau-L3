import java.io.*;
import java.net.*;
import java.util.Scanner;

public class c {

    public static void main(String[] args) throws IOException {

        if (args.length<2) {
            System.out.println("Saisir le serveur et le port");
        }
        Scanner scanner =  new Scanner(System.in);
        InetSocketAddress adress = new InetSocketAddress(1234);
        Socket s = new Socket();
        s.connect(adress);
        System.out.print("Choose a username :");
        String username = scanner.nextLine();
        String message ="@"+username+"#";
        OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
        osw.write("@"+username + "\n");
        osw.flush();

        // Créer un thread pour recevoir les messages du serveur
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiveThread.start();

        while(scanner.hasNextLine()) {
            String textImput = scanner.nextLine();
            message= message +textImput+"\n";
             osw = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
            osw.write(message);
            osw.flush();
            message ="@"+username+"#";
        }
    }
}
