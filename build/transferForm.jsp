<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@ taglib uri="jakarta.tags.core" prefix="c" %> <jsp:include page="header.jsp"/>

<!-- Error Alert (If transfer fails) -->
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show">
        ${errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="row">
    <!-- Sidebar: Accounts List with Sort Only -->
    <div class="col-md-4">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-dark text-white">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span><i class="fas fa-wallet me-2"></i> Accounts</span>
                </div>
                
                <!-- Sort Buttons -->
                <div class="btn-group w-100" role="group">
                    <button type="button" class="btn btn-sm btn-secondary" onclick="sortList('id')">Sort ID</button>
                    <button type="button" class="btn btn-sm btn-secondary" onclick="sortList('bal')">Sort Bal</button>
                </div>
            </div>
            
            <ul class="list-group list-group-flush overflow-auto" id="accountList" style="max-height: 500px;">
                <c:forEach var="acc" items="${accountsList}">
                    <li class="list-group-item d-flex justify-content-between align-items-center account-item">
                        <span class="small">${acc}</span>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>

    <!-- Form Area -->
    <div class="col-md-8">
        <div class="card shadow-sm p-4">
            <h4 class="mb-3">Execute Transfer</h4>
            <form action="Controller" method="post">
                <input type="hidden" name="action" value="processTransfer">
                
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Source Account ID</label>
                        <input type="number" class="form-control form-control-lg" name="sourceId" placeholder="From ID" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Destination Account ID</label>
                        <input type="number" class="form-control form-control-lg" name="destId" placeholder="To ID" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Amount ($)</label>
                        <div class="input-group input-group-lg">
                            <span class="input-group-text">$</span>
                            <input type="number" class="form-control" name="amount" step="0.01" required>
                        </div>
                    </div>
                </div>
                
                <button type="submit" class="btn btn-success btn-lg w-100 mt-4">
                    <i class="fas fa-paper-plane me-2"></i> Transfer Funds
                </button>
            </form>
        </div>
    </div>
</div>

<!-- Success Modal -->
<c:if test="${not empty transferSuccess}">
<div class="modal fade" id="successModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header bg-success text-white">
        <h5 class="modal-title">Transfer Successful</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body text-center">
        <i class="fas fa-check-circle fa-4x text-success mb-3"></i>
        <h5>Transaction Complete</h5>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<script>
    window.onload = function() {
        var myModal = new bootstrap.Modal(document.getElementById('successModal'));
        myModal.show();
    };
</script>
</c:if>

<!-- Client-Side Scripts for Sort -->
<script>
    // State to track sort direction (asc by default)
    let sortDir = { id: 'asc', bal: 'asc' };

    // Sort Logic
    function sortList(type) {
        let list = document.getElementById('accountList');
        let items = Array.from(list.getElementsByTagName('li'));
        
        // Toggle direction
        let currentDir = sortDir[type];
        let multiplier = (currentDir === 'asc') ? 1 : -1;
        
        items.sort((a, b) => {
            let textA = a.innerText; 
            let textB = b.innerText;
            // Parse format: "1 | Checking | Active | $1500.00"
            let partsA = textA.split('|');
            let partsB = textB.split('|');
            
            if(type === 'id') {
                // Sort by Account ID (Index 0)
                return (parseInt(partsA[0]) - parseInt(partsB[0])) * multiplier;
            } else if(type === 'bal') {
                // Sort by Balance (Index 3)
                let balA = parseFloat(partsA[3].replace('$','').replace(',','').trim());
                let balB = parseFloat(partsB[3].replace('$','').replace(',','').trim());
                return (balA - balB) * multiplier; 
            }
        });
        
        // Flip direction for next click
        sortDir[type] = (currentDir === 'asc') ? 'desc' : 'asc';
        
        // Re-append items in new order
        items.forEach(item => list.appendChild(item));
    }
</script>
<jsp:include page="footer.jsp"/>