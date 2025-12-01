import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing operations on the LOAN table.
 * Implements CRUD and the specialized updateLoanStatus functionality.
 */
public class LoanDAO {

    // --- CREATE Operation ---
    /**
     * Inserts a new loan record.
     */
    public boolean addLoan(int customerId, String loanType, double loanAmount, double interestRate, String status, String startDate) {
        String sql = "INSERT INTO LOAN (customer_id, loan_type, loan_amount, interest_rate, status, start_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setString(2, loanType);
            stmt.setDouble(3, loanAmount);
            stmt.setDouble(4, interestRate);
            stmt.setString(5, status);
            stmt.setString(6, startDate);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            // printing the trace
        }
        return false;
    }

    // --- READ Operation ---
    /**
     * Retrieves all loan records from the database.
     * Returns a list of formatted strings for the command-line interface.
     */
    public List<String> getAllLoans() {
        List<String> loans = new ArrayList<>();
        String sql = "SELECT * FROM LOAN";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String loan = rs.getInt("loan_id") + " | Cust ID: " +
                              rs.getInt("customer_id") + " | Type: " +
                              rs.getString("loan_type") + " | Amount: $" +
                              rs.getDouble("loan_amount") + " | Status: " +
                              rs.getString("status");

                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // --- UPDATE Operation (Required Key Feature) ---
    /**
     * Updates the status of a specific loan.
     */
    public boolean updateLoanStatus(int loanId, String newStatus) {
        String sql = "UPDATE LOAN SET status = ? WHERE loan_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // The new status must match an ENUM value (e.g., "Approved", "Paid")
            stmt.setString(1, newStatus); 
            stmt.setInt(2, loanId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // --- DELETE Operation ---
    /**
     * Deletes a loan record by its ID.
     */
    public boolean deleteLoan(int loanId) {
        String sql = "DELETE FROM LOAN WHERE loan_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loanId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}