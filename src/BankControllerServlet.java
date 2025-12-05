import java.io.IOException; import java.util.ArrayList; import java.util.List; import jakarta.servlet.ServletException; import jakarta.servlet.http.HttpServlet; import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse;
/**
  * BankControllerServlet
  * This Servlet acts as the main Controller in the MVC architecture.
  * It intercepts all HTTP requests (GET and POST), determines the requested 
  * "action" via a parameter, interacts with the Data Access Objects (DAOs), 
  * and forwards the user to the appropriate JSP view.
  */
public class BankControllerServlet extends HttpServlet {

private AccountDAO accountDAO;
private CustomerDAO customerDAO;
private TransactionService txService;
private LoanDAO loanDAO;
/**
  * Initializes the Servlet and instantiates the necessary helper classes.
  * This method runs once when the Servlet container loads the class.
  */
public void init() throws ServletException {
    accountDAO = new AccountDAO();
    customerDAO = new CustomerDAO();
    txService = new TransactionService();
    loanDAO = new LoanDAO();
}

// handles HTTP POST requests by forwarding to processRequest
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    processRequest(request, response);
}

// handles HTTP GET requests by forwarding to processRequest
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    processRequest(request, response);
}

/**
  * Central processing method for all requests.
  * Switches on the 'action' parameter to determine business logic.
  * @param request  The HttpServletRequest object
  * @param response The HttpServletResponse object
  */
protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String action = request.getParameter("action");
    if (action == null) action = ""; 
    
    try {
        switch (action) {
            
            // --- VIEW ACCOUNTS ---
            case "viewAccounts":
                request.setAttribute("pageTitle", "View All Accounts");
                request.setAttribute("accountsList", accountDAO.getAllAccounts());
                request.getRequestDispatcher("/viewAccounts.jsp").forward(request, response);
                break;

            // --- SEARCH ACCOUNT (SMARTER LOGIC) ---
            case "searchAccount":
                String query = request.getParameter("searchQuery");
                request.setAttribute("pageTitle", "Search Results");
                
                List<String> allAccounts = accountDAO.getAllAccounts();
                List<String> filteredList = new ArrayList<>();
                
                if (query != null && !query.trim().isEmpty()) {
                    String q = query.trim();
                    boolean isNumeric = q.matches("\\d+"); // Check if query is just numbers
                    
                    for (String acc : allAccounts) {
                        // Format: ID | Type | Status | $Balance
                        // split to search specific columns
                        String[] parts = acc.split("\\|"); 
                        if (parts.length >= 2) {
                            String id = parts[0].trim();
                            String type = parts[1].trim();
                            
                            if (isNumeric) {
                                // Exact match for ID (Fixes the "1 matches 10 and 1500" issue)
                                if (id.equals(q)) {
                                    filteredList.add(acc);
                                }
                            } else {
                                // Case-insensitive partial match for Type
                                if (type.toLowerCase().contains(q.toLowerCase())) {
                                    filteredList.add(acc);
                                }
                            }
                        }
                    }
                    request.setAttribute("message", "Found " + filteredList.size() + " result(s) for '" + query + "'");
                } else {
                    filteredList = allAccounts; 
                }
                
                request.setAttribute("accountsList", filteredList);
                request.getRequestDispatcher("/viewAccounts.jsp").forward(request, response);
                break;

            // --- DEACTIVATE ACCOUNT ---
            case "deleteAccount": 
                int delAccId = Integer.parseInt(request.getParameter("accountId"));
                boolean accDeactivated = accountDAO.deactivateAccount(delAccId);
                
                request.setAttribute("pageTitle", "View All Accounts");
                request.setAttribute("accountsList", accountDAO.getAllAccounts());
                
                if(accDeactivated) {
                    request.setAttribute("message", "✅ Account #" + delAccId + " has been set to Inactive.");
                } else {
                    request.setAttribute("message", "❌ Failed to deactivate Account #" + delAccId);
                }
                request.getRequestDispatcher("/viewAccounts.jsp").forward(request, response);
                break;

            // --- ADD CUSTOMER ---
            case "addCustomerForm":
                request.setAttribute("pageTitle", "Register New Customer");
                request.getRequestDispatcher("/customerForm.jsp").forward(request, response);
                break;

            case "addCustomer":
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String dob = request.getParameter("dob");
                String address = request.getParameter("address");
                
                String initBalStr = request.getParameter("initialBalance");
                String incomeStr = request.getParameter("income");
                String creditStr = request.getParameter("creditScore");
                // capture Account Type
                String accType = request.getParameter("accountType");
                if(accType == null || accType.isEmpty()) accType = "Checking"; 

                double initialBal = (initBalStr != null && !initBalStr.isEmpty()) ? Double.parseDouble(initBalStr) : 0.0;
                double income = (incomeStr != null && !incomeStr.isEmpty()) ? Double.parseDouble(incomeStr) : 0.0;
                int creditScore = (creditStr != null && !creditStr.isEmpty()) ? Integer.parseInt(creditStr) : 0;

                boolean customerAdded = customerDAO.addCustomer(firstName, lastName, email, phone, dob, address, income, creditScore);
                
                String message = "";
                if (customerAdded) {
                    int newCustId = customerDAO.getLatestCustomerId();
                    if (newCustId != -1) {
                        String today = java.time.LocalDate.now().toString();
                        accountDAO.addAccount(newCustId, accType, initialBal, today);
                        message = "✅ Customer registered and " + accType + " Account created with $" + initialBal;
                    }
                } else {
                    message = "❌ Customer Add Failed (Email might be duplicate).";
                }
                
                request.setAttribute("pageTitle", "Transaction Result");
                request.setAttribute("message", message);
                request.getRequestDispatcher("/result.jsp").forward(request, response);
                break;

            // --- TRANSFERS ---
            case "loadTransferForm":
                request.setAttribute("pageTitle", "Fund Transfer");
                request.setAttribute("accountsList", accountDAO.getAllAccounts()); 
                request.getRequestDispatcher("/transferForm.jsp").forward(request, response);
                break;

            case "processTransfer":
                int sourceId = Integer.parseInt(request.getParameter("sourceId"));
                int destId = Integer.parseInt(request.getParameter("destId"));
                double amount = Double.parseDouble(request.getParameter("amount"));
                
                boolean success = txService.transferFunds(sourceId, destId, amount);

                if (success) {
                    request.setAttribute("transferSuccess", true);
                    request.setAttribute("pageTitle", "Fund Transfer");
                    request.setAttribute("accountsList", accountDAO.getAllAccounts()); 
                    request.getRequestDispatcher("/transferForm.jsp").forward(request, response);
                } else {
                    // FAILURE CASE FIX: Stay on transfer page
                    request.setAttribute("pageTitle", "Fund Transfer");
                    request.setAttribute("accountsList", accountDAO.getAllAccounts()); 
                    request.setAttribute("errorMessage", "❌ TRANSFER FAILED! Check IDs, Balance, or Status.");
                    request.getRequestDispatcher("/transferForm.jsp").forward(request, response);
                }
                break;
                
            // --- LOANS ---
            case "loanForm":
                request.setAttribute("pageTitle", "Loan Status Management");
                request.setAttribute("loansList", loanDAO.getAllLoans());
                request.getRequestDispatcher("/loanForm.jsp").forward(request, response);
                break;

            case "updateLoanStatus":
                int loanId = Integer.parseInt(request.getParameter("loanId"));
                String newStatus = request.getParameter("newStatus");
                
                boolean loanUpdated = loanDAO.updateLoanStatus(loanId, newStatus);
                
                if (loanUpdated) {
                    request.setAttribute("pageTitle", "Loan Status Management");
                    request.setAttribute("loansList", loanDAO.getAllLoans());
                    request.getRequestDispatcher("/loanForm.jsp").forward(request, response);
                } else {
                    request.setAttribute("pageTitle", "Error");
                    request.setAttribute("message", "Loan Status Update Failed.");
                    request.getRequestDispatcher("/result.jsp").forward(request, response);
                }
                break;

            case "deleteLoan":
                int delLoanId = Integer.parseInt(request.getParameter("loanId"));
                boolean loanDeleted = loanDAO.deleteLoan(delLoanId);
                
                request.setAttribute("pageTitle", "Loan Status Management");
                request.setAttribute("loansList", loanDAO.getAllLoans());
                if(loanDeleted) request.setAttribute("message", "✅ Loan #" + delLoanId + " deleted.");
                request.getRequestDispatcher("/loanForm.jsp").forward(request, response);
                break;
                
            default:
                response.sendRedirect("index.jsp");
                break;
        }
    } catch (NumberFormatException e) {
        request.setAttribute("pageTitle", "Error Result");
        request.setAttribute("message", "Error: Invalid ID or Amount format. Please use numbers only.");
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }
}
}
