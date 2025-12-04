<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@ taglib uri="jakarta.tags.core" prefix="c" %> <%@ taglib uri="jakarta.tags.functions" prefix="fn" %> <jsp:include page="header.jsp"/>

<div class="row mb-4 align-items-center">
    <!-- Server-Side Search Form -->
    <div class="col-md-8">
        <form action="Controller" method="GET" class="d-flex">
            <input type="hidden" name="action" value="searchAccount">
            <input type="text" name="searchQuery" class="form-control me-2" placeholder="Search by ID or Type (e.g. 'Checking')..." required>
            <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Search</button>
            <a href="Controller?action=viewAccounts" class="btn btn-outline-secondary ms-2">Reset</a>
        </form>
    </div>
    <div class="col-md-4 text-end">
        <a href="Controller?action=addCustomerForm" class="btn btn-success">
            <i class="fas fa-plus"></i> Register Customer
        </a>
    </div>
</div>

<!-- Feedback Message (Success/Error) -->
<c:if test="${not empty message}">
    <div class="alert alert-info alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover table-striped mb-0" id="accountsTable">
                <thead class="table-dark">
                    <tr>
                        <!-- Clickable Headers for JS Sorting -->
                        <th onclick="sortTable(0)" style="cursor:pointer">Account ID ↕</th>
                        <th onclick="sortTable(1)" style="cursor:pointer">Type ↕</th>
                        <th onclick="sortTable(2)" style="cursor:pointer">Status ↕</th>
                        <th onclick="sortTable(3)" style="cursor:pointer">Balance ↕</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="accountString" items="${accountsList}">
                        <%-- Data Format from DAO: ID | Type | Status | $Balance --%>
                        <c:set var="parts" value="${fn:split(accountString, ' | ')}" />
                        <tr>
                            <td class="font-monospace">${parts[0]}</td>
                            <td>${parts[1]}</td>
                            
                            <!-- Dynamic Status Badge -->
                            <td>
                                <span class="badge ${parts[2] == 'Active' ? 'bg-success' : 'bg-danger'}">
                                    ${parts[2]}
                                </span>
                            </td>
                            
                            <td class="fw-bold text-success">${parts[3]}</td>
                            
                            <!-- Deactivate Button (Only if Active) -->
                            <td>
                                <c:if test="${parts[2] eq 'Active'}">
                                    <form action="Controller" method="POST" onsubmit="return confirm('Deactivate Account #${parts[0]}? No funds can be transferred to/from it.');">
                                        <input type="hidden" name="action" value="deleteAccount"> 
                                        <input type="hidden" name="accountId" value="${parts[0]}">
                                        <button class="btn btn-sm btn-outline-warning" title="Deactivate">
                                            <i class="fas fa-ban"></i> Deactivate
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${parts[2] ne 'Active'}">
                                    <span class="text-muted small">Disabled</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty accountsList}">
                        <tr><td colspan="5" class="text-center text-muted p-4">No accounts found matching your criteria.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- JavaScript for Client-Side Sorting -->
<script>
    function sortTable(n) {
        var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
        table = document.getElementById("accountsTable");
        switching = true;
        dir = "asc"; 
        while (switching) {
            switching = false;
            rows = table.rows;
            for (i = 1; i < (rows.length - 1); i++) {
                shouldSwitch = false;
                x = rows[i].getElementsByTagName("TD")[n];
                y = rows[i + 1].getElementsByTagName("TD")[n];
                
                let xVal = x.innerHTML.replace('$','').replace(',','').trim();
                let yVal = y.innerHTML.replace('$','').replace(',','').trim();
                
                let xNum = parseFloat(xVal);
                let yNum = parseFloat(yVal);
                
                if (!isNaN(xNum) && !isNaN(yNum)) {
                    if (dir == "asc") { if (xNum > yNum) { shouldSwitch = true; break; } }
                    else { if (xNum < yNum) { shouldSwitch = true; break; } }
                } else {
                    if (dir == "asc") { if (xVal.toLowerCase() > yVal.toLowerCase()) { shouldSwitch = true; break; } }
                    else { if (xVal.toLowerCase() < yVal.toLowerCase()) { shouldSwitch = true; break; } }
                }
            }
            if (shouldSwitch) {
                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                switching = true;
                switchcount ++; 
            } else {
                if (switchcount == 0 && dir == "asc") {
                    dir = "desc";
                    switching = true;
                }
            }
        }
    }
</script>
<jsp:include page="footer.jsp"/>