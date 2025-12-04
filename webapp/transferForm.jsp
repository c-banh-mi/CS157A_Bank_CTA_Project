<%@ taglib uri="jakarta.tags.core" prefix="c" %> <jsp:include page="header.jsp"/>

<div class="row">
    <!-- Sidebar: Accounts List -->
    <div class="col-md-4">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-dark text-white">
                <i class="fas fa-wallet me-2"></i> Accounts & Balances
            </div>
            <ul class="list-group list-group-flush overflow-auto" style="max-height: 500px;">
                <c:forEach var="acc" items="${accountsList}">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
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

<!-- Success Modal Popup -->
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
        <hr>
        <div class="d-flex justify-content-around align-items-center">
            <div>
                <h6>Source Account</h6>
                <span class="text-muted text-decoration-line-through">Old Balance</span>
                <br>
                <i class="fas fa-arrow-down text-danger"></i>
                <br>
                <span class="fw-bold">New Balance</span>
            </div>
            <i class="fas fa-chevron-right fa-2x text-muted"></i>
            <div>
                <h6>Dest Account</h6>
                <span class="text-muted text-decoration-line-through">Old Balance</span>
                <br>
                <i class="fas fa-arrow-up text-success"></i>
                <br>
                <span class="fw-bold">New Balance</span>
            </div>
        </div>
        <p class="text-muted mt-3 small">Updates have been committed to the database.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<script>
    // Auto-trigger modal on page load
    window.onload = function() {
        var myModal = new bootstrap.Modal(document.getElementById('successModal'));
        myModal.show();
    };
</script>
</c:if>
<jsp:include page="footer.jsp"/>