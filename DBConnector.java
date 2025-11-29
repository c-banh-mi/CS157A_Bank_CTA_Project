import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnector {

    private static final String URL = "URL";
    private static final String USER = "Username";
    private static final String PASSWORD = "Password";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}