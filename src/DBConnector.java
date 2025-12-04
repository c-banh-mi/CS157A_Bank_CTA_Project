import java.sql.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBConnector {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/BankCTA_DB?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "wu9r*6rEDAJ8";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";

    private DBConnector() {}

    static {
        if (!DRIVER.isEmpty()) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getEnvOrProp(String key, String defaultValue) {
        String v = System.getProperty(key);
        if (v == null || v.isEmpty()) v = System.getenv(key);
        return (v == null || v.isEmpty()) ? defaultValue : v;
    }

    public static Connection getConnection() throws SQLException {
        if (USER.isEmpty() && PASSWORD.isEmpty()) {
            return DriverManager.getConnection(CONNECTION_URL);
        } else {
            return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
        }
    }
    
    private static Connection getBaseConnection() throws SQLException {
        if (USER.isEmpty() && PASSWORD.isEmpty()) {
            return DriverManager.getConnection(BASE_URL);
        } else {
            return DriverManager.getConnection(BASE_URL, USER, PASSWORD);
        }
    }

    public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        return executeQuery(sql, new Object[0]);
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= cols; i++) {
                        String label = md.getColumnLabel(i);
                        Object value = rs.getObject(i);
                        row.put(label, value);
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }

    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    public static void closeQuietly(AutoCloseable... resources) {
        if (resources == null) return;
        for (AutoCloseable r : resources) {
            if (r == null) continue;
            try {
                r.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static void executeSQLFile(String filePath) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                int commentIndex = line.indexOf("--");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                    if (line.isEmpty()) continue;
                }

                sql.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sqlToExecute = sql.toString().trim();
                    System.out.println("Executing SQL: " + sqlToExecute);
                    stmt.execute(sqlToExecute);
                    sql.setLength(0);
                }
            }

            if (sql.length() > 0) {
                String sqlToExecute = sql.toString().trim();
                System.out.println("Executing SQL: " + sqlToExecute);
                stmt.execute(sqlToExecute);
            }

            System.out.println("Executed SQL file: " + filePath);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        try (Connection baseConn = getBaseConnection();
             Statement stmt = baseConn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS BankCTA_DB"); 
            System.out.println("Database BankCTA_DB created or already exists.");
            
        } catch (SQLException e) {
            System.err.println("Error during database creation.");
            e.printStackTrace();
        }
        
        try {
            executeSQLFile("db/create_schema.sql"); 
            executeSQLFile("db/initialize_data.sql");
            executeSQLFile("db/sample_queries.sql");

            List<Map<String, Object>> rows = executeQuery("SELECT * FROM ACCOUNT WHERE customer_id = 1");
            System.out.println("Test Query Result (Customer ID 1 Accounts):");
            for (Map<String, Object> row : rows) {
                System.out.println(row);
            }

        } catch (SQLException e) {
            System.err.println("Error during initial database setup. Could not create or populate schema.");
            e.printStackTrace();
        }
    }
}