import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionService {

    private AccountDAO accountDAO = new AccountDAO();

    public boolean transferFunds(int sourceId, int destId, double amount) {
        // Prevent transfer if either account is Inactive
        if (!accountDAO.isAccountActive(sourceId)) {
            System.err.println("Transfer Failed: Source Account #" + sourceId + " is Inactive.");
            return false;
        }
        if (!accountDAO.isAccountActive(destId)) {
            System.err.println("Transfer Failed: Destination Account #" + destId + " is Inactive.");
            return false;
        }

        // Proceed with Transaction Logic wiht Standard Debit/Credit
        Connection conn = null;
        try {
            conn = DBConnector.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            // Check Balance
            double sourceBalance = 0;
            String checkSql = "SELECT balance FROM ACCOUNT WHERE account_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, sourceId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    sourceBalance = rs.getDouble("balance");
                }
            }

            if (sourceBalance < amount) {
                conn.rollback();
                return false;
            }

            // Debit Source
            String debitSql = "UPDATE ACCOUNT SET balance = balance - ? WHERE account_id = ?";
            try (PreparedStatement debitStmt = conn.prepareStatement(debitSql)) {
                debitStmt.setDouble(1, amount);
                debitStmt.setInt(2, sourceId);
                debitStmt.executeUpdate();
            }

            // Credit Destination
            String creditSql = "UPDATE ACCOUNT SET balance = balance + ? WHERE account_id = ?";
            try (PreparedStatement creditStmt = conn.prepareStatement(creditSql)) {
                creditStmt.setDouble(1, amount);
                creditStmt.setInt(2, destId);
                creditStmt.executeUpdate();
            }

            // Record Transaction
            String logSql = "INSERT INTO TRANSACTION_RECORD (source_account_id, destination_account_id, amount, transaction_type, transaction_date) VALUES (?, ?, ?, 'Transfer', NOW())";
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, sourceId);
                logStmt.setInt(2, destId);
                logStmt.setDouble(3, amount);
                logStmt.executeUpdate();
            }

            conn.commit(); // Commit Transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DBConnector.closeQuietly(conn);
        }
        return false;
    }
}
