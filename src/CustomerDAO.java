import java.sql.*; import java.util.ArrayList; import java.util.List;

public class CustomerDAO {

/**
 * Inserts a new customer into the CUSTOMER table with financial details.
 * @return true if insert succeeded, false otherwise
 */
public boolean addCustomer(String firstName, String lastName, String email,
                           String phone, String dob, String address,
                           double income, int creditScore) {

    // Updated SQL to include income and credit_score columns
    String sql = "INSERT INTO CUSTOMER (first_name, last_name, email, phone_number, dob, address, income, credit_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, phone);
        stmt.setString(5, dob);
        stmt.setString(6, address);
        // Set the new financial values
        stmt.setDouble(7, income);
        stmt.setInt(8, creditScore);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

/**
 * Retrieves a single customer using their customer_id.
 * @param customerId ID of the customer to retrieve
 * @return ResultSet containing the customer record
 */
public ResultSet getCustomerById(int customerId) {
    String sql = "SELECT * FROM CUSTOMER WHERE customer_id = ?";

    try {
        Connection conn = DBConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerId);
        return stmt.executeQuery();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

/**
 * Retrieves all customers in the table.
 * @return List of formatted customer strings
 */
public List<String> getAllCustomers() {
    List<String> customers = new ArrayList<>();
    String sql = "SELECT * FROM CUSTOMER";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String c = rs.getInt("customer_id") + " | " +
                       rs.getString("first_name") + " " +
                       rs.getString("last_name");

            customers.add(c);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return customers;
}

/**
 * Updates a customer's email using their ID.
 * @return true if update succeeded
 */
public boolean updateCustomerEmail(int customerId, String newEmail) {
    String sql = "UPDATE CUSTOMER SET email = ? WHERE customer_id = ?";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, newEmail);
        stmt.setInt(2, customerId);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

/**
 * Deletes a customer by ID.
 */
public boolean deleteCustomer(int customerId) {
    String sql = "DELETE FROM CUSTOMER WHERE customer_id = ?";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, customerId);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

/**
 * Gets the most recently created Customer ID.
 * Used to link a new Account to the new Customer.
 */
public int getLatestCustomerId() {
    String sql = "SELECT MAX(customer_id) FROM CUSTOMER";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Return -1 on failure
}
}