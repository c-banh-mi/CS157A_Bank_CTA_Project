import java.sql.*; import java.util.ArrayList; import java.util.List;

/**
 * Data Access Object (DAO) for managing operations on the ACCOUNT table.
 * Handles CRUD operations and account status updates.
 */
public class AccountDAO {

/**
  * Creates a new account for a specific customer.
  * The 'status' column defaults to 'Active' in the database schema.
  *
  * @param customerId  The ID of the customer owning the account.
  * @param accountType The type of account (Checking/Savings).
  * @param balance     The initial deposit amount.
  * @param openDate    The date the account was opened.
  * @return true if the account was successfully created, false otherwise.
  */
public boolean addAccount(int customerId, String accountType, double balance, String openDate) {
    String sql = "INSERT INTO ACCOUNT (customer_id, account_type, balance, open_date) VALUES (?, ?, ?, ?)";
    // Try-with-resources ensures Connection and PreparedStatement are closed automatically
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
  * Retrieves a list of all accounts in the database, including their current status.
  * Used to populate the admin dashboard view.
  *
  * @return A List of formatted strings (ID | Type | Status | Balance).
  */
public List<String> getAllAccounts() {
    List<String> accounts = new ArrayList<>();
    // Select specific columns to minimize data transfer overhead
    String sql = "SELECT account_id, account_type, status, balance FROM ACCOUNT";
    
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            // Format: ID | Type | Status | $Balance
            // Format the data into a pipe-delimited string for easy parsing in the JSP
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
  * Deactivates an account by setting its status to 'Inactive'.
  * This is a "Soft Delete" operation to preserve transaction history integrity
  * and satisfy Foreign Key constraints in the TRANSACTION_RECORD table.
  *
  * @param accountId The ID of the account to deactivate.
  * @return true if the update was successful.
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
  * Checks if a specific account is currently marked as 'Active'.
  * This helper method is used by TransactionService to validate funds transfers.
  *
  * @param accountId The account ID to check.
  * @return true if the account exists and status is 'Active', false otherwise.
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

 /**
   * Updates the balance of a specific account.
   * Typically called during a Fund Transfer or Loan Disbursement.
   *
   * @param accountId The account to update.
   * @param newBalance The new balance value to set. 
   * @return true if the update succeeded.
   */
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
