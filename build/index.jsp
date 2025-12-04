<jsp:include page="header.jsp"/> <!-- Override title for direct access --> <script>document.title = "Bank CTA - Dashboard";</script>

<div class="text-center mb-5">
    <h1 class="display-4 text-primary fw-bold">Bank CTA</h1>
    <h3 class="text-secondary fw-light">Client Transaction Administration</h3>
    <p class="lead mt-3">Secure Banking Management System</p>
</div>

<div class="row g-4">
    <!-- Card 1: View Accounts -->
    <div class="col-md-6">
        <a href="Controller?action=viewAccounts" class="text-decoration-none">
            <div class="card h-100 shadow-sm border-0 border-start border-primary border-5">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="bg-primary text-white rounded-circle p-3 me-3">
                            <i class="fas fa-users fa-2x"></i>
                        </div>
                        <h4 class="card-title text-dark m-0">View All Accounts</h4>
                    </div>
                    <p class="card-text text-muted">Access complete database of customer accounts, view current balances, and monitor account types.</p>
                </div>
            </div>
        </a>
    </div>

    <!-- Card 2: Fund Transfer -->
    <div class="col-md-6">
        <a href="Controller?action=loadTransferForm" class="text-decoration-none">
            <div class="card h-100 shadow-sm border-0 border-start border-success border-5">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="bg-success text-white rounded-circle p-3 me-3">
                            <i class="fas fa-money-bill-wave fa-2x"></i>
                        </div>
                        <h4 class="card-title text-dark m-0">Fund Transfer</h4>
                    </div>
                    <p class="card-text text-muted">Initiate secure transactions between accounts with real-time balance updates and verification.</p>
                </div>
            </div>
        </a>
    </div>

    <!-- Card 3: Add Customer -->
    <div class="col-md-6">
        <a href="Controller?action=addCustomerForm" class="text-decoration-none">
            <div class="card h-100 shadow-sm border-0 border-start border-info border-5">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="bg-info text-white rounded-circle p-3 me-3">
                            <i class="fas fa-user-plus fa-2x"></i>
                        </div>
                        <h4 class="card-title text-dark m-0">Add New Customer</h4>
                    </div>
                    <p class="card-text text-muted">Onboard new clients, assign initial deposit balances, and record KYC information (SSN, Income).</p>
                </div>
            </div>
        </a>
    </div>

    <!-- Card 4: Loan Admin -->
    <div class="col-md-6">
        <a href="Controller?action=loanForm" class="text-decoration-none">
            <div class="card h-100 shadow-sm border-0 border-start border-warning border-5">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="bg-warning text-white rounded-circle p-3 me-3">
                            <i class="fas fa-file-contract fa-2x"></i>
                        </div>
                        <h4 class="card-title text-dark m-0">Loan Administration</h4>
                    </div>
                    <p class="card-text text-muted">Review pending loan applications. Approve or reject based on credit score and income metrics.</p>
                </div>
            </div>
        </a>
    </div>
</div>
<jsp:include page="footer.jsp"/>