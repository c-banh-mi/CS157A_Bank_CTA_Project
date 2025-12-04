<%@ taglib uri="jakarta.tags.core" prefix="c" %> <%@ taglib uri="jakarta.tags.functions" prefix="fn" %> <jsp:include page="header.jsp"/>

<div class="alert alert-info shadow-sm mb-4">
    <div class="d-flex align-items-center">
        <i class="fas fa-info-circle fa-2x me-3"></i>
        <div>
            <strong>Approval Metrics:</strong> 
            Loans are automatically flagged based on Debt-to-Income (DTI) ratio and Credit Score.
            <br>
            <small>Approval Recommended if: Credit Score > 700 AND Income > $40,000.</small>
        </div>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
        <span>Loan Applications</span>
        <span class="badge bg-warning text-dark">Pending Review</span>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
                <tr>
                    <th>Loan ID</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th class="text-end">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="loan" items="${loansList}">
                    <c:set var="parts" value="${fn:split(loan, '|')}" />
                    <tr>
                        <td>#${parts[0]}</td>
                        <td>${parts[1]}</td>
                        <td class="fw-bold">${parts[2]}</td>
                        
                        <td>
                            <span class="badge rounded-pill 
                                ${parts[3] == 'Approved' ? 'bg-success' : 
                                  parts[3] == 'Rejected' ? 'bg-danger' : 
                                  parts[3] == 'Paid' ? 'bg-info' : 'bg-warning text-dark'}">
                                ${parts[3]}
                            </span>
                        </td>
                        <td class="text-end">
                            <div class="btn-group btn-group-sm">
                                <form action="Controller" method="POST" class="d-inline">
                                    <input type="hidden" name="action" value="updateLoanStatus">
                                    <input type="hidden" name="loanId" value="${parts[0]}">
                                    <input type="hidden" name="newStatus" value="Approved">
                                    <button class="btn btn-outline-success" title="Approve">
                                        <i class="fas fa-check"></i>
                                    </button>
                                </form>
                                <form action="Controller" method="POST" class="d-inline">
                                    <input type="hidden" name="action" value="updateLoanStatus">
                                    <input type="hidden" name="loanId" value="${parts[0]}">
                                    <input type="hidden" name="newStatus" value="Rejected">
                                    <button class="btn btn-outline-danger" title="Reject">
                                        <i class="fas fa-times"></i>
                                    </button>
                                </form>
                                <form action="Controller" method="POST" class="d-inline">
                                    <input type="hidden" name="action" value="updateLoanStatus">
                                    <input type="hidden" name="loanId" value="${parts[0]}">
                                    <input type="hidden" name="newStatus" value="Paid">
                                    <button class="btn btn-outline-info" title="Mark Paid">
                                        <i class="fas fa-dollar-sign"></i>
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<jsp:include page="footer.jsp"/>