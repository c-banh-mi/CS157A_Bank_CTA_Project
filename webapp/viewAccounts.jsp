<html>
    <head><title>View All Accounts</title></head>
    <body>
        <h2>All Accounts</h2>
        <c:forEach var="account" items="${accountsList}">
            <p>${account}</p> 
        </c:forEach>
        <a href="index.jsp">Back to Menu</a>
    </body>
</html>