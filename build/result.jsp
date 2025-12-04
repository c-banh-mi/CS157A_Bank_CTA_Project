<%@ taglib uri="jakarta.tags.core" prefix="c" %> <%@ taglib uri="jakarta.tags.functions" prefix="fn" %> <jsp:include page="header.jsp"/>

<%-- Logic to determine alert color based on the message text --%>
<c:set var="alertClass" value="info" /> <%-- Default to blue/info --%>

<c:if test="${fn:startsWith(message, '✅')}">
    <c:set var="alertClass" value="success" />
</c:if>

<c:if test="${fn:startsWith(message, 'Error:') || fn:startsWith(message, '❌')}">
    <c:set var="alertClass" value="danger" />
</c:if>

<div class="card shadow-sm text-center p-5">
    <div class="card-body">
        <h3 class="card-title mb-4">Transaction Status</h3>
        
        <div class="alert alert-${alertClass}" role="alert">
            <p class="mb-0 fs-5">${message}</p>
        </div>

        <a href="index.jsp" class="btn btn-secondary mt-3">Return to Dashboard</a>
    </div>
</div>
<jsp:include page="footer.jsp"/>