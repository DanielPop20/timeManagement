<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Time Management Application</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <header class="bg-primary text-white py-3">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center">
                <h1 class="mb-0">Time Management Application</h1>
                <nav>
                    <ul class="nav">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="index.jsp">Home</a>
                        </li>
                        <c:if test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link text-white" href="dashboard.jsp">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="calendar.jsp">Calendar</a>
                            </li>
                            <li class="nav-item">
                                <!-- Logout button styled as red -->
                                <button class="btn btn-danger" id="logoutButton">Logout</button>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.user == null}">
                            <li class="nav-item">
                                <a class="nav-link text-white" href="register.jsp">Register</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="login.jsp">Login</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </header>

    <!-- Optional JavaScript -->
    <!-- Bootstrap JS and dependencies -->
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="static/js/user.js"></script>
</body>
</html>
