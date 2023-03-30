import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataBaseRequests {

    Connection conn;
    Statement stmt;


    public int findId() throws SQLException {
        int id = 0;
        ResultSet set = stmt.executeQuery("Select count(ID) AS NumberOfIds from MESSAGES;");
        while (set.next()) id = Integer.parseInt(set.getString("NumberOfIds"));
        return id + 1;
    }

    public DataBaseRequests() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BlogMessages", "sofiane", "8dd457sw");
        stmt = conn.createStatement();
    }

    public void closeBD() throws SQLException {
        conn.close();
    }

    public void updateData(String request) {
        try {
            stmt.executeUpdate(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Integer> selectDataID(String request, int limit, String tags) {
        ResultSet set;
        ArrayList<Integer> id = new ArrayList<>();
        String message;
        int n = 0;
        String[] tagsList = null;
        if (tags != null) tagsList = tags.substring(1).split(" ");

        try {
            set = stmt.executeQuery(request);

            while (set.next() && n < limit) {
                if (tagsList != null) {
                    message = (set.getString("MESSAGE"));

                    if (Arrays.stream(tagsList).anyMatch(message::contains)) {
                        id.add(set.getInt("ID"));
                        n++;
                    }
                } else {
                    id.add(set.getInt("ID"));
                    n++;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }


    public ArrayList<String> selectData(String request, String columnLabel) {
        ArrayList<String> messages = new ArrayList<>();

        ResultSet set;
        try {
            set = stmt.executeQuery(request);
            while (set.next()) {
                messages.add(set.getString(columnLabel));
            }
        } catch (SQLException e) {
            System.out.println("No date Found !");
        }
        return messages;
    }


}

