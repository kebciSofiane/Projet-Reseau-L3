import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MicroblogCentralClient {

    public static void main(String[] args) throws IOException, SQLException {

        Scanner scanner =  new Scanner(System.in);
        InetSocketAddress adress = new InetSocketAddress(12345);
        Socket s = new Socket();
        s.connect(adress);
        System.out.print("Choose a username :");
        String username = scanner.nextLine();
        String message ="PUBLISH @"+username+"#";
        OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
        osw.write("@"+username + "\n");
        osw.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        System.out.println(reader.readLine());


        DataBaseRequests dataBaseRequests= new DataBaseRequests();

        int request;
        do {
            System.out.println("--------------------");
            ArrayList<String> myTags = dataBaseRequests.selectData("Select Tag from TAGS where USERNAME='@"+username+"';","TAG");
            ArrayList<String> myFavUsers = dataBaseRequests.selectData("Select USER from USERS where USERNAME='@"+username+"';","USER");
            System.out.println("My tags : "+myTags);
            System.out.println("My Favorite users : "+myFavUsers);
            System.out.println("--------------------");

            do {
                System.out.println("What do you want to do ?");
                System.out.println("1-Publish messages");
                System.out.println("2-Reply to a message");
                System.out.println("3-Republish a message");
                System.out.println("4-Subscribe to a tag/user");
                System.out.println("5-Unsubscribe to a tag/user");
                System.out.println("00-Quit");

                request = Integer.parseInt(scanner.nextLine());

            } while (request != 1 && request!=2 && request!=3  && request!=4 && request!=5 && request!= 0);


            switch (request) {
                case 1 :
                    System.out.println("Your messages (-SKIP- to stop sending) :");
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
                    while(true) {
                        String textImput = scanner.nextLine();
                        message= message +textImput+"\n";
                        if (Objects.equals(textImput, "SKIP")){
                            break;
                        }
                        osw = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
                        osw.write(message);
                        osw.flush();
                        message ="PUBLISH @"+username+"#";
                    }
                    break;
                case 2:
                    RequestReply requestReply =new RequestReply(s);
                    requestReply.reply(username);
                    break;

                case 3:
                    scanner =  new Scanner(System.in);
                    System.out.print("The republished message id :");
                    int id = Integer.parseInt(scanner.nextLine());
                    RequestRepublish requestRepublish = new RequestRepublish(s);
                    requestRepublish.republish(username,id);
                    break;

                case 4 :
                    String answer;
                    do {
                        System.out.println("Subscribe to a tag ? y/n");
                        answer = scanner.nextLine().toLowerCase();
                    } while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n"));
                    if (answer.equals("y"))
                    {
                        String tag ;
                        System.out.println("Tags (space between each one) :");
                        tag = scanner.nextLine().toLowerCase();
                        String[] tagsList= tag.split(" ");
                        for (int i=0; i<tagsList.length;i++)
                            dataBaseRequests.updateData("insert into TAGS values('@"+username+"','"+tagsList[i]+"');");
                    }
                    do {
                        System.out.println("Subscribe to a user ? y/n");
                        answer = scanner.nextLine().toLowerCase();
                    } while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n"));
                    if (answer.equals("y"))
                    {
                        String user ;
                        System.out.println("Users (space between each one) :");
                        user = scanner.nextLine().toLowerCase();
                        String[] usersList= user.split(" ");
                        for (int i=0; i<usersList.length;i++)
                            dataBaseRequests.updateData("insert into USERS values('@"+username+"','@"+usersList[i]+"');");
                    }
                    break;
                case 5 :
                    do {
                        System.out.println("Unsubscribe to a tag ? y/n");
                        answer = scanner.nextLine().toLowerCase();
                    } while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n"));
                    if (answer.equals("y"))
                    {
                        String tag ;
                        System.out.println("Tags (space between each one) :");
                        tag = scanner.nextLine().toLowerCase();
                        String[] tagsList= tag.split(" ");
                        for (int i=0; i<tagsList.length;i++)
                            dataBaseRequests.updateData
                                    ("delete from TAGS where username='@"+username+"' and TAG='"+tagsList[i]+"';");
                    }
                    do {
                        System.out.println("Subscribe to a user ? y/n");
                        answer = scanner.nextLine().toLowerCase();
                    } while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n"));
                    if (answer.equals("y"))
                    {
                        String user ;
                        System.out.println("Users (space between each one) :");
                        user = scanner.nextLine().toLowerCase();
                        String[] usersList= user.split(" ");
                        for (int i=0; i<usersList.length;i++)
                            dataBaseRequests.updateData
                                    ("delete from USERS where username='@"+username+"' and USER='"+usersList[i]+"';");
                    }


            }
        } while (request != 0);


    }

}
