<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calendar View</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/6.1.15/index.min.css">
    <style>
        .calendar-container {
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    <div class="container calendar-container">
        <div id="calendar"></div>
    </div>
    <script src="static/js/tasks.js"></script>
</body>
</html>
