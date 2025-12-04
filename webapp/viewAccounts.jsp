<%@ taglib uri="jakarta.tags.core" prefix="c" %> <%@ taglib uri="jakarta.tags.functions" prefix="fn" %> <jsp:include page="header.jsp"/>

<div class="d-flex justify-content-end mb-3">
    <a href="Controller?action=addCustomerForm" class="btn btn-outline-primary">
        <i class="fas fa-plus"></i> Register New Customer
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive" style="max-height: 70vh;">
            <table class="table table-hover table-striped mb-0">
                <thead class="table-dark sticky-top">
                    <tr>
                        <th>Account ID</th>
                        <th>Customer ID</th>
                        <th>Type</th>
                        <th>Balance</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="accountString" items="${accountsList}">
                        <c:set var="parts" value="${fn:split(accountString, ' | ')}" />
                        <tr>
                            <!-- Masked ID logic (*****123) -->
                            <td class="font-monospace">*****${parts[0]}</td>
                            <!-- Assuming parts[1] is type or custID depending on your DAO string -->
                            <td>${parts[1]}</td> 
                            <td><span class="badge bg-secondary">Active</span></td>
                            <td class="fw-bold text-success">${parts[2]}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty accountsList}">
                        <tr><td colspan="4" class="text-center text-muted p-4">No accounts found in database.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>