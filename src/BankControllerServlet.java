import java.io.IOException; import jakarta.servlet.ServletException; import jakarta.servlet.http.HttpServlet; import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse;

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
                request.setAttribute("pageTitle", "View All Accounts");
                request.setAttribute("accountsList", accountDAO.getAllAccounts());
                request.getRequestDispatcher("/viewAccounts.jsp").forward(request, response);
                break;

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
                    
                    // 1. Capture Financials
                    String initBalStr = request.getParameter("initialBalance");
                    String incomeStr = request.getParameter("income");
                    String creditStr = request.getParameter("creditScore");

                    double initialBal = (initBalStr != null && !initBalStr.isEmpty()) ? Double.parseDouble(initBalStr) : 0.0;
                    double income = (incomeStr != null && !incomeStr.isEmpty()) ? Double.parseDouble(incomeStr) : 0.0;
                    int creditScore = (creditStr != null && !creditStr.isEmpty()) ? Integer.parseInt(creditStr) : 0;

                    // 2. Create Customer (Now passes income and creditScore)
                    boolean customerAdded = customerDAO.addCustomer(firstName, lastName, email, phone, dob, address, income, creditScore);
                    
                    String message = "";
                    if (customerAdded) {
                        // 3. Auto-Create Account
                        int newCustId = customerDAO.getLatestCustomerId();
                        if (newCustId != -1) {
                            String today = java.time.LocalDate.now().toString();
                            accountDAO.addAccount(newCustId, "Checking", initialBal, today);
                            message = "✅ Customer registered (Credit Score: " + creditScore + ") and Checking Account created.";
                        }
                    } else {
                        message = "❌ Customer Add Failed.";
                    }
                    
                    request.setAttribute("pageTitle", "Transaction Result");
                    request.setAttribute("message", message);
                    request.getRequestDispatcher("/result.jsp").forward(request, response);
                    break;

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
                    // Set success flag to trigger modal in JSP
                    request.setAttribute("transferSuccess", true);
                    request.setAttribute("pageTitle", "Fund Transfer");
                    // Refresh the list for the sidebar
                    request.setAttribute("accountsList", accountDAO.getAllAccounts()); 
                    request.getRequestDispatcher("/transferForm.jsp").forward(request, response);
                } else {
                    request.setAttribute("pageTitle", "Transaction Failed");
                    request.setAttribute("message", "❌ TRANSFER FAILED! Funds rolled back.");
                    request.getRequestDispatcher("/result.jsp").forward(request, response);
                }
                break;
                
            case "loanForm":
                request.setAttribute("pageTitle", "Loan Status Management");
                request.setAttribute("loansList", loanDAO.getAllLoans());
                request.getRequestDispatcher("/loanForm.jsp").forward(request, response);
                break;

            case "updateLoanStatus":
                int loanId = Integer.parseInt(request.getParameter("loanId"));
                String newStatus = request.getParameter("newStatus");
                
                boolean loanUpdated = loanDAO.updateLoanStatus(loanId, newStatus);
                
                // If updated, reload the loan form to see changes immediately
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