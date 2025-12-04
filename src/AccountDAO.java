import java.sql.*; import java.util.ArrayList; import java.util.List;

public class AccountDAO {

/**
 * Creates a new account. Status defaults to 'Active' in the database.
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
        e.printStackTrace();
    }
    return false;
}

/**
 * Retrieves all accounts with their Status.
 */
public List<String> getAllAccounts() {
    List<String> accounts = new ArrayList<>();
    // UPDATED: Now selects the 'status' column
    String sql = "SELECT account_id, account_type, status, balance FROM ACCOUNT";
    
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            // Format: ID | Type | Status | $Balance
            // This format matches what viewAccounts.jsp expects for splitting
            String a = rs.getInt("account_id") + " | " +
                       rs.getString("account_type") + " | " +
                       rs.getString("status") + " | $" +
                       String.format("%.2f", rs.getDouble("balance"));
            accounts.add(a);
        }
    } catch (SQLException e) {
        System.err.println("!!! ACCOUNTDAO ERROR: Failed to get accounts.");
        e.printStackTrace();
    }
    return accounts;
}

/**
 * NEW: Deactivates an account instead of deleting it.
 * This preserves transaction history while preventing future use.
 */
public boolean deactivateAccount(int accountId) {
    String sql = "UPDATE ACCOUNT SET status = 'Inactive' WHERE account_id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, accountId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

/**
 * NEW: Helper to check if an account is Active.
 * Used by TransactionService to validate transfers.
 */
public boolean isAccountActive(int accountId) {
    String sql = "SELECT status FROM ACCOUNT WHERE account_id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, accountId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String status = rs.getString("status");
                return "Active".equalsIgnoreCase(status);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Return false if account doesn't exist or error occurs
}

// Updates balance (used for transfers)
public boolean updateBalance(int accountId, double newBalance) {
    String sql = "UPDATE ACCOUNT SET balance = ? WHERE account_id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDouble(1, newBalance);
        stmt.setInt(2, accountId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}