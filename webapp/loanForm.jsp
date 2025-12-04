<form action="Controller" method="POST">
    <h2>Update Loan Status</h2>
    <input type="hidden" name="action" value="updateLoanStatus">
    Loan ID: <input type="number" name="loanId" required><br>
    New Status: 
    <select name="newStatus">
        <option value="Approved">Approved</option>
        <option value="Rejected">Rejected</option>
        <option value="Paid">Paid</option>
        <option value="Pending">Pending</option>
    </select><br>
    <button type="submit">Update Status</button>
</form>