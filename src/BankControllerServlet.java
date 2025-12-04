import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Import your business logic classes
import your_package.AccountDAO;
import your_package.CustomerDAO;
import your_package.TransactionService;
import your_package.LoanDAO;
import java.sql.SQLException;

public class BankControllerServlet extends HttpServlet {

    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    private TransactionService txService;
    private LoanDAO loanDAO;

    public void init() throws ServletException {
        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();
        txService = new TransactionService();
        loanDAO = new LoanDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = ""; 
        
        try {
            switch (action) {
                
                case "viewAccounts":
                    // Retrieve all accounts from DAO and set as attribute for JSP
                    request.setAttribute("accountsList", accountDAO.getAllAccounts());
                    request.getRequestDispatcher("/viewAccounts.jsp").forward(request, response);
                    break;

                case "addCustomer":
                    // Logic to process customer form submission
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String dob = request.getParameter("dob");
                    String address = request.getParameter("address");

                    boolean customerAdded = customerDAO.addCustomer(firstName, lastName, email, phone, dob, address);
                    
                    request.setAttribute("message", customerAdded ? "Customer Added Successfully!" : "Customer Add Failed (Email unique constraint?).");
                    request.getRequestDispatcher("/result.jsp").forward(request, response);
                    break;

                case "processTransfer":
                    // --- TRANSACTIONAL LOGIC INTEGRATION (CRITICAL) ---
                    int sourceId = Integer.parseInt(request.getParameter("sourceId"));
                    int destId = Integer.parseInt(request.getParameter("destId"));
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    
                    boolean success = txService.transferFunds(sourceId, destId, amount);

                    String message = success ? "✅ Transfer SUCCESSFUL! Changes saved." : "❌ TRANSFER FAILED! Funds rolled back.";
                    
                    request.setAttribute("message", message);
                    request.getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                
                case "updateLoanStatus":
                    // Logic to process loan status update form
                    int loanId = Integer.parseInt(request.getParameter("loanId"));
                    String newStatus = request.getParameter("newStatus");
                    
                    boolean loanUpdated = loanDAO.updateLoanStatus(loanId, newStatus);
                    
                    request.setAttribute("message", loanUpdated ? "Loan Status Updated Successfully." : "Loan Status Update Failed.");
                    request.getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                    
                default:
                    response.sendRedirect("index.jsp");
                    break;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Error: Invalid ID or Amount format. Please use numbers only.");
            request.getRequestDispatcher("/result.jsp").forward(request, response);
        } catch (SQLException e) {
            // Catch SQL exceptions that weren't caught in the DAO/Service layers
            request.setAttribute("message", "Database Error: " + e.getMessage());
            request.getRequestDispatcher("/result.jsp").forward(request, response);
        }
    }
}