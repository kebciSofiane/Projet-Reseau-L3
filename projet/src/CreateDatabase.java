import java.sql.*;
public class CreateDatabase
{
    public static void main(String args[])
    {
        try
        {
            //étape 1: charger la classe driver
            Class.forName("com.mysql.jdbc.Driver");
            //étape 2: créer l'objet de connexion
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:1234/mydt", "1mkll2345", "");
            //étape 3: créer l'objet statement
            Statement stmt = conn.createStatement();
            //étape 4: exécuter la requéte
            System.out.println("Création de base de données...");
            stmt.executeUpdate("CREATE DATABASE emp");
            System.out.println("Base de données crée avec succés...");
            //étape 5: fermez l'objet de connexion
            conn.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
