import java.sql.*;

public class DataBaseRequests
{

     Connection conn;
     Statement stmt;
     String message ="" ;



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
    public String selectDataID(String request) {
        ResultSet set;
        try {
            set = stmt.executeQuery(request);
            while(set.next()) message = message +"-"+(set.getInt("id"));
            //System.out.println("Update successful");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }


    public String selectDataMessage(String request) {
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
