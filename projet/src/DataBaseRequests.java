import java.sql.*;
import java.util.Arrays;

public class DataBaseRequests
{

     Connection conn;
     Statement stmt;



    public int findId() throws SQLException {
        int id = 0;
        ResultSet set = stmt.executeQuery("Select count(ID) AS NumberOfIds from MESSAGES;");
        while(set.next()) id = Integer.parseInt(set.getString("NumberOfIds"));
        return id+1;
    }

    public DataBaseRequests() throws SQLException {
         conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BlogMessages", "sofiane", "8dd457sw");
         stmt = conn.createStatement();
    }

    public void closeBD() throws SQLException {
        conn.close();
    }
    public  void updateData(String request) {
        /*stmt.executeUpdate("create table MESSAGES (" +
                "ID int PRIMARY KEY AUTO_INCREMENT," +
                "USERNAME VARCHAR(20)," +
                "MESSAGE TINYTEXT " +
                ");");*/
        try {
            stmt.executeUpdate(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public String selectDataID(String request,int limit,String tags) {
        ResultSet set;
        StringBuilder id = new StringBuilder();
        String message;
        int n=0;
        String[] tagsList = null;
        if (tags!= null) tagsList =tags.split(" ");
        try {
            set = stmt.executeQuery(request);

            while(set.next() && n<limit){
                message = (set.getString("MESSAGE"));

                if (tagsList!= null){
                    System.out.println(tagsList[0]);
                    if ( Arrays.stream(tagsList).anyMatch(message::contains)){
                         System.out.println("jnfjkn");
                         id.append("-").append(set.getInt("id"));
                         n++;
                }}
                else {
                         id.append("-").append(set.getInt("id"));
                         n++;
                     }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id.toString();
    }


    public String selectDataMessage(String request) {
        String message ="" ;
        ResultSet set;
        try {
            set = stmt.executeQuery(request);
            while(set.next()){
                message =(set.getString("MESSAGE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
    }
