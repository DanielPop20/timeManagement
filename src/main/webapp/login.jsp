<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Time Management</title>
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    <main class="container mt-5">
        <h2 class="mb-4">Login</h2>
        <form id="loginForm" action="user" method="post" class="mt-4">
            <input type="hidden" name="action" value="login">

            <div class="form-group mb-3">
                <label for="username" class="form-label">Username:</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Enter your username" required>
            </div>

            <div class="form-group mb-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn btn-primary">Login</button>
        </form>

        <c:if test="${param.error == 'true'}">
            <div class="alert alert-danger mt-3">
                <p>Login failed. Please check your username and password.</p>
            </div>
        </c:if>
    </main>
</body>
</html>
