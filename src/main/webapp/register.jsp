<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Time Management</title>
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    <main class="container mt-5">
        <h2 class="mb-4">Register</h2>

        <c:choose>
            <c:when test="${param.success == 'true'}">
                <div class="alert alert-success mt-3">
                    <p>Registration successful! You can now <a href="login.jsp" class="alert-link">log in</a>.</p>
                </div>
            </c:when>
            <c:when test="${param.error == 'true'}">
                <div class="alert alert-danger mt-3">
                    <p>Registration failed. Please try again.</p>
                </div>
            </c:when>
        </c:choose>

        <form id="registerForm" action="user" method="post" class="mt-4">
            <input type="hidden" name="action" value="register">

            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Enter your username" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email" required>
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn btn-primary mt-3">Register</button>
        </form>
    </main>
</body>
</html>
