import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // --- CREATE Operation ---
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
        }
        return false;
    }

    // --- READ Operation (With JOIN) ---
    public List<String> getAllLoans() {
        List<String> loans = new ArrayList<>();
        // JOIN query to get Loan info + Customer Income/Score
        String sql = "SELECT l.loan_id, l.loan_type, l.loan_amount, l.status, " +
                     "c.customer_id, c.income, c.credit_score " +
                     "FROM LOAN l " +
                     "JOIN CUSTOMER c ON l.customer_id = c.customer_id";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Format: ID | Type | Amount | Status | CustID | Income | Score
                String loan = rs.getInt("loan_id") + "|" + 
                              rs.getString("loan_type") + "|" +
                              String.format("$%.2f", rs.getDouble("loan_amount")) + "|" +
                              rs.getString("status") + "|" +
                              rs.getInt("customer_id") + "|" +
                              String.format("$%.2f", rs.getDouble("income")) + "|" +
                              rs.getInt("credit_score");
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // --- UPDATE Operation ---
    public boolean updateLoanStatus(int loanId, String newStatus) {
        String sql = "UPDATE LOAN SET status = ? WHERE loan_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus); 
            stmt.setInt(2, loanId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // --- DELETE Operation (This was missing!) ---
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