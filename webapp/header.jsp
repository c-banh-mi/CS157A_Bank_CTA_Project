<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>

<html lang="en"> <head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Bank CTA - <c:out value="${pageTitle}" default="Dashboard"/></title> <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"> <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"> <style> body { min-height: 100vh; background-color: #f4f6f9; font-family: sans-serif; } .sidebar { background-color: #2c3e50; min-height: 100vh; padding-top: 20px; width: 250px; position: fixed; top: 0; left: 0; z-index: 1000; } .sidebar h4 { color: #ecf0f1; border-bottom: 1px solid #34495e; padding-bottom: 15px; text-align: center; } .sidebar a { color: #bdc3c7; padding: 12px 15px; text-decoration: none; display: block; border-radius: 4px; transition: 0.3s; } .sidebar a:hover { background-color: #34495e; color: #fff; transform: translateX(5px); } .sidebar .btn-back { background-color: #c0392b; color: white; text-align: center; margin-top: auto; } .sidebar .btn-back:hover { background-color: #e74c3c; } .content-wrapper { padding-left: 250px; width: 100%; } .main-content { padding: 30px; } </style> </head> <body> <div class="d-flex"> <!-- Sidebar --> <div class="sidebar d-flex flex-column p-3"> <h4 class="mb-4">Menu</h4> <a href="Controller?action=viewAccounts"><i class="fas fa-list me-2"></i> View Accounts</a> <a href="Controller?action=loadTransferForm"><i class="fas fa-exchange-alt me-2"></i> Fund Transfer</a> <a href="Controller?action=addCustomerForm"><i class="fas fa-user-plus me-2"></i> Add Customer</a> <a href="Controller?action=loanForm"><i class="fas fa-file-invoice-dollar me-2"></i> Loan Admin</a>

        <div class="mt-auto">
            <hr class="border-secondary">
            <a href="index.jsp" class="btn-back">Back to Main Menu</a>
        </div>
    </div>
    
    <!-- Content Start -->
    <div class="content-wrapper">
        <div class="main-content">
            <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                <h1><c:out value="${pageTitle}" default="Bank CTA"/></h1>
            </div>