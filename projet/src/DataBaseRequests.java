import java.sql.*;
public class DataBaseRequests
{

    static int id;
    public static int findId(){
        id =id+1;
        return id;
    }
    public static void updateData(String request) {
        try
        {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/BlogMessages", "sofiane", "8dd457sw");
            Statement stmt = conn.createStatement();

            /*stmt.executeUpdate("create table MESSAGES (" +
                    "ID int PRIMARY KEY AUTO_INCREMENT," +
                    "USERNAME VARCHAR(20)," +
                    "MESSAGE VARCHAR(255) " +
                    ");");*/

            stmt.executeUpdate(request);

            //stmt.executeUpdate("INSERT INTO MESSAGES VALUES(2,'@Mériem','hiiiii')");

            System.out.println("Update successful");
            conn.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
