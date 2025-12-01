import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

// This service handles complex business logic, particularly transactions that 
// require multiple database operations to be treated as a single atomic unit.
public class TransactionService {

    // --- SQL Constants for Atomic Operations ---
    private static final String DEBIT_SQL = 
        "UPDATE ACCOUNT SET balance = balance - ? WHERE account_id = ?";
    private static final String CREDIT_SQL = 
        "UPDATE ACCOUNT SET balance = balance + ? WHERE account_id = ?";
    private static final String RECORD_SQL = 
        "INSERT INTO TRANSACTION_RECORD (source_account_id, destination_account_id, amount, transaction_type, transaction_date) VALUES (?, ?, ?, ?, NOW())";

    
    /**
     * Executes a fund transfer between two accounts as a single, atomic database transaction.
     * If the debit, credit, or record fails, all changes are rolled back.
     * * @param sourceId The account ID to debit.
     * @param destId The account ID to credit.
     * @param amount The amount to transfer.
     * @return true if the transfer was successful, false otherwise.
     */
    public boolean transferFunds(int sourceId, int destId, double amount) {
        // Must declare Connection outside try-block to access it in the catch/finally blocks
        Connection conn = null; 

        try {
            conn = DBConnector.getConnection();
            conn.setAutoCommit(false); // 1. START Transaction Management

            // --- 2. DEBIT SOURCE ACCOUNT ---
            try (PreparedStatement psDebit = conn.prepareStatement(DEBIT_SQL)) {
                psDebit.setDouble(1, amount);
                psDebit.setInt(2, sourceId);
                
                // Ensure a row was updated (account exists)
                if (psDebit.executeUpdate() == 0) { 
                    throw new SQLException("Debit failed. Source account " + sourceId + " not found.");
                }
            }
            
            // --- 3. CREDIT DESTINATION ACCOUNT ---
            try (PreparedStatement psCredit = conn.prepareStatement(CREDIT_SQL)) {
                psCredit.setDouble(1, amount);
                psCredit.setInt(2, destId);
                
                // Ensure a row was updated (destination account exists)
                if (psCredit.executeUpdate() == 0) { 
                    throw new SQLException("Credit failed. Destination account " + destId + " not found.");
                }
            }

            // --- 4. RECORD TRANSACTION ---
            try (PreparedStatement psRecord = conn.prepareStatement(RECORD_SQL)) {
                psRecord.setInt(1, sourceId);
                psRecord.setInt(2, destId);
                psRecord.setDouble(3, amount);
                psRecord.setString(4, "Transfer"); // Must match the ENUM value
                psRecord.executeUpdate();
            }

            conn.commit(); // 5. COMMIT changes if all 3 steps succeeded
            return true;

        } catch (SQLException e) {
            System.err.println("Transaction failed. Rolling back changes.");
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 6. ROLLBACK if ANY step failed
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            // 7. Reset and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Return connection to default state
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}