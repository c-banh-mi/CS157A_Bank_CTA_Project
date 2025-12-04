<jsp:include page="header.jsp"/>

<div class="card shadow-sm border-0">
    <div class="card-body p-5">
        <h4 class="card-title mb-4">Client Onboarding Form</h4>
        <form action="Controller" method="POST">
            <input type="hidden" name="action" value="addCustomer">
            
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">First Name</label>
                    <input type="text" class="form-control" name="firstName" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Last Name</label>
                    <input type="text" class="form-control" name="lastName" required>
                </div>
                
                <div class="col-md-6">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Phone</label>
                    <input type="text" class="form-control" name="phone" required placeholder="XXX-XXX-XXXX">
                </div>

                <div class="col-md-6">
                    <label class="form-label">Date of Birth</label>
                    <input type="date" class="form-control" name="dob" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label text-danger">Social Security Number (SSN) *</label>
                    <input type="password" class="form-control" name="ssn" placeholder="XXX-XX-XXXX">
                    <div class="form-text">Mandatory for US Banking compliance.</div>
                </div>

                <div class="col-12">
                    <label class="form-label">Address</label>
                    <input type="text" class="form-control" name="address" required>
                </div>

                <div class="col-12"><hr></div>
                <h5 class="text-primary">Financial Profile</h5>

                <div class="col-md-4">
                    <label class="form-label">Initial Deposit ($)</label>
                    <input type="number" class="form-control" name="initialBalance" min="0" step="0.01" value="0.00">
                </div>
                <div class="col-md-4">
                    <label class="form-label">Annual Income ($)</label>
                    <input type="number" class="form-control" name="income" min="0">
                </div>
                <div class="col-md-4">
                    <label class="form-label">Credit Score</label>
                    <input type="number" class="form-control" name="creditScore" min="300" max="850">
                </div>
            </div>
            
            <div class="d-grid gap-2 mt-5">
                <button type="submit" class="btn btn-primary btn-lg">Create Customer Profile</button>
            </div>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>