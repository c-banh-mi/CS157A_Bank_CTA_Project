import java.sql.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing database connections and executing SQL scripts.
 * This class follows the Singleton pattern conceptually (though static methods are used)
 * to provide a centralized point for database interaction.
 */
public final class DBConnector {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/BankCTA_DB?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";

    private DBConnector() {}
    // Static block to load the MySQL JDBC driver when the class is loaded
    static {
        if (!DRIVER.isEmpty()) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets environment variable or system property, falling back to a default value.
     * Useful for configuring database credentials via environment variables.
     */
    private static String getEnvOrProp(String key, String defaultValue) {
        String v = System.getProperty(key);
        if (v == null || v.isEmpty()) v = System.getenv(key);
        return (v == null || v.isEmpty()) ? defaultValue : v;
    }
    /**
     * Establishes a connection to the specific application database (BankCTA_DB).
     * This method is used by all DAOs to perform CRUD operations.
     *
     * @return A Connection object to the BankCTA_DB.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (USER.isEmpty() && PASSWORD.isEmpty()) {
            return DriverManager.getConnection(CONNECTION_URL);
        } else {
            return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
        }
    }
    /**
     * Establishes a connection to the MySQL server without specifying a database.
     * This is exclusively used during the initialization phase to create the
     * database schema if it does not already exist.
     *
     * @return A Connection object to the MySQL server root.
     * @throws SQLException If a database access error occurs.
     */
    private static Connection getBaseConnection() throws SQLException {
        if (USER.isEmpty() && PASSWORD.isEmpty()) {
            return DriverManager.getConnection(BASE_URL);
        } else {
            return DriverManager.getConnection(BASE_URL, USER, PASSWORD);
        }
    }
    /**
     * Helper method to execute a SELECT query without parameters.
     */
    public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        return executeQuery(sql, new Object[0]);
    }
    /**
     * Executes a parameterized SELECT query and returns the results as a List of Maps.
     * Each Map represents a row, where keys are column names and values are the data.
     * This generic method allows DAOs to retrieve data without writing repetitive JDBC boilerplate.
     *
     * @param sql    The SQL query string with placeholders (?).
     * @param params The values to fill the placeholders.
     * @return A List of Maps containing the query results.
     * @throws SQLException If a database error occurs.
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
     * Executes an INSERT, UPDATE, or DELETE query.
     *
     * @param sql    The SQL statement string.
     * @param params The values for the placeholders.
     * @return The number of rows affected.
     * @throws SQLException If a database error occurs.
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }
    /**
     * Sets the parameters for a PreparedStatement.
     * Handles the mapping of Java objects to SQL types.
     */
    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
    /**
     * Utility to safely close AutoCloseable resources (Connections, Statements, ResultSets)
     * without throwing checked exceptions.
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
    /**
     * Reads and executes SQL commands from a file (e.g., schema creation scripts).
     * This method parses the file, removes comments, and executes statements delimited by semicolons.
     *
     * @param filePath The relative path to the SQL file.
     */
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
    /**
     * Main method for initializing the database environment.
     * Run this class directly to create the database and tables, and insert sample data.
     */
    public static void main(String[] args) {
        // create the Database if it doesn't exist
        try (Connection baseConn = getBaseConnection();
             Statement stmt = baseConn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS BankCTA_DB"); 
            System.out.println("Database BankCTA_DB created or already exists.");
            
        } catch (SQLException e) {
            System.err.println("Error during database creation.");
            e.printStackTrace();
        }
        // create tables and populate data using the new database connection
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
