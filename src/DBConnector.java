import java.sql.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple DB connector utility.
 *
 * Configuration via system properties or environment variables:
 * - DB_URL (default: jdbc:mysql:bank.db)
 * - DB_USER
 * - DB_PASSWORD
 * - DB_DRIVER (optional: e.g. com.mysql.cj.jdbc.Driver)
 *
 * Usage:
 * Connection c = DBConnector.getConnection();
 * List<Map<String,Object>> rows = DBConnector.executeQuery("SELECT * FROM accounts WHERE id = ?", 1);
 */
public final class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/bank?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "-ango451236ANGO";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


    private DBConnector() { /* utility */ }

    static {
        if (!DRIVER.isEmpty()) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                // Driver not found; applications may still work if JDBC auto-loading is available.
                e.printStackTrace();
            }
        }
    }

    private static String getEnvOrProp(String key, String defaultValue) {
        String v = System.getProperty(key);
        if (v == null || v.isEmpty()) v = System.getenv(key);
        return (v == null || v.isEmpty()) ? defaultValue : v;
    }

    /**
     * Obtain a new connection. Caller is responsible for closing it.
     */
    public static Connection getConnection() throws SQLException {
        if (USER.isEmpty() && PASSWORD.isEmpty()) {
            return DriverManager.getConnection(URL);
        } else {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    /**
     * Execute a parameterized SELECT and return rows as a list of maps (column label -> value).
     * Resources are closed automatically.
     */
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

    /**
     * Execute an INSERT/UPDATE/DELETE. Returns affected row count.
     */
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

    /**
     * Close resources quietly (ignores exceptions).
     */
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
                if (line.isEmpty()) continue; // skip blank lines

                // Remove inline comments starting with --
                int commentIndex = line.indexOf("--");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                    if (line.isEmpty()) continue; // skip line if nothing left
                }

                sql.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sqlToExecute = sql.toString().trim();
                    System.out.println("Executing SQL: " + sqlToExecute);
                    stmt.execute(sqlToExecute);
                    sql.setLength(0); // reset for next statement
                }
            }

            // Execute any remaining SQL not terminated with ;
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
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            // Create the database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS bank");
            System.out.println("Database created or already exists.");

            // Optional: switch to the 'bank' database
            stmt.execute("USE bank");

            // Execute SQL files
            executeSQLFile("db/create_schema.sql");       // Create tables
            executeSQLFile("db/initialize_data.sql");    // Insert data
            executeSQLFile("db/sample_queries.sql");     // Optional queries

            // Test query
            List<Map<String, Object>> rows = executeQuery("SELECT * FROM ACCOUNT WHERE customer_id = 1");
            for (Map<String, Object> row : rows) {
                System.out.println(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}