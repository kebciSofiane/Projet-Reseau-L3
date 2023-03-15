import java.sql.*;
import java.util.ArrayList;

public class DataBaseRequests
{

     Connection conn;
     Statement stmt;
    public int findId() throws SQLException {
        int id = 0;
        ResultSet set = stmt.executeQuery("Select count(ID) AS NumberOfIds from MESSAGES;");
        while(set.next()){
            id = Integer.parseInt(set.getString("NumberOfIds"));
        }
        System.out.println(id);
        return id+1;
    }

    public DataBaseRequests() throws SQLException {
         conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BlogMessages", "sofiane", "8dd457sw");
         stmt = conn.createStatement();    }

    public  void updateData(String request) {
        /*stmt.executeUpdate("create table MESSAGES (" +
                "ID int PRIMARY KEY AUTO_INCREMENT," +
                "USERNAME VARCHAR(20)," +
                "MESSAGE TINYTEXT " +
                ");");*/
        //stmt.executeUpdate("INSERT INTO MESSAGES VALUES(2,'@Mériem','hiiiii')");
        //System.out.println("Update successful");
        try {
            stmt.executeUpdate(request);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    String f="" ;
    public String selectData(String request) {
        ResultSet set;
        try {
            set = stmt.executeQuery(request);
            while(set.next()){
                f=f+"-"+(set.getInt("id"));
                System.out.print("From: " + set.getString("USERNAME"));
                System.out.print(", ID: " + set.getInt("id"));
                System.out.println(", Message: " + set.getString("MESSAGE"));
            }
            System.out.println("Update successful");
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return f;
    }

    }
