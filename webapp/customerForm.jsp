<form action="Controller" method="POST">
    <h2>Add New Customer</h2>
    <input type="hidden" name="action" value="addCustomer">
    First Name: <input type="text" name="firstName" required><br>
    Last Name: <input type="text" name="lastName" required><br>
    Email: <input type="email" name="email" required><br>
    Phone: <input type="text" name="phone" required><br>
    DOB: <input type="date" name="dob" required><br>
    Address: <input type="text" name="address" required><br>
    <button type="submit">Register Customer</button>
</form>