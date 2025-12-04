import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /**
     * Creates a new account for a customer.
     */
    public boolean addAccount(int customerId, String accountType, double balance, String openDate) {
        String sql = "INSERT INTO ACCOUNT (customer_id, account_type, balance, open_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setString(2, accountType);
            stmt.setDouble(3, balance);
            stmt.setString(4, openDate);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Log the error for debugging
            System.err.println("ACCOUNTDAO ERROR: Failed to add account.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves an account by its account_id.
     * ⚠️ FIX: Modified to return a String/Account object instead of a raw ResultSet
     * ⚠️ FIX: Applied try-with-resources to ensure connection closure
     */
    // This method is primarily used internally, so we'll just check existence here for simplicity.
    // NOTE: This method had a resource leak (Connection was not closed). It should not return a raw ResultSet.
    public boolean accountExists(int accountId) {
        String sql = "SELECT account_id FROM ACCOUNT WHERE account_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // True if account exists
            }

        } catch (SQLException e) {
            System.err.println("ACCOUNTDAO ERROR: Failed to check account existence.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves every account in the ACCOUNT table.
     * ⚠️ FIX: Added debug logging for silent errors.
     */
    public List<String> getAllAccounts() {
        List<String> accounts = new ArrayList<>();
        String sql = "SELECT account_id, account_type, balance FROM ACCOUNT";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Ensure column names are correct as per your schema
                String a = rs.getInt("account_id") + " | " +
                           rs.getString("account_type") + " | $" +
                           String.format("%.2f", rs.getDouble("balance")); // Format currency

                accounts.add(a);
            }

        } catch (SQLException e) {
            // This error is likely why your page is empty. Print it prominently.
            System.err.println("!!! ACCOUNTDAO FATAL ERROR: Database connection or query failed. Check credentials/schema.");
            e.printStackTrace();
            // The method returns an empty list upon failure, as before.
        }

        return accounts;
    }

    /**
     * Updates the balance of an account.
     */
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE ACCOUNT SET balance = ? WHERE account_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("ACCOUNTDAO ERROR: Failed to update balance.");
            e.printStackTrace();
        }
        return false;
    }

    // Delete Account
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM ACCOUNT WHERE account_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("ACCOUNTDAO ERROR: Failed to delete account.");
            e.printStackTrace();
        }
        return false;
    }
}