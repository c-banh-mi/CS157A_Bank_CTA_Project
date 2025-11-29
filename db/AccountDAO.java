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
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves an account by its account_id.
     */
    public ResultSet getAccountById(int accountId) {
        String sql = "SELECT * FROM ACCOUNT WHERE account_id = ?";

        try {
            Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves every account in the ACCOUNT table.
     */
    public List<String> getAllAccounts() {
        List<String> accounts = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String a = rs.getInt("account_id") + " | " +
                           rs.getString("account_type") + " | $" +
                           rs.getDouble("balance");

                accounts.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return false;
    }
}
