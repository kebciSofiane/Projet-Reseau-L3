import java.sql.*;
public class CreateDatabase
{
    public static void main(String args[])
    {
        try
        {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/BlogMessages", "sofiane", "8dd457sw");
            Statement stmt = conn.createStatement();


            stmt.executeUpdate("CREATE TABLE Messages (\n" +
                    "    id int,\n" +
                    "    message varchar(259)\n" +
                    ");");
            conn.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
