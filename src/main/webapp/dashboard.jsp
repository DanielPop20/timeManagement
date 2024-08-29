<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Management</title>
    <style>
        .task-card {
            margin-bottom: 15px;
        }
        .form-group.required .control-label:after {
            content: "*";
            color: red;
        }
        .calendar-container {
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <%@ include file="/common/header.jsp" %>

    <div class="container">
        <h1 class="mt-4 mb-4">All your tasks</h1>

        <jsp:include page="/common/filterSection.jsp" />

        <div id="task-container" class="row">
            <c:forEach var="task" items="${tasks}">
                <div class="col-md-4 mb-4">
                    <div class="card task-card" id="task-${task.id}">
                        <div class="card-body">
                            <h5 class="card-title">${task.title}</h5>
                            <p class="card-text">${task.description}</p>
                            <p class="card-text"><strong>Category:</strong> ${task.category}</p>
                            <p class="card-text"><strong>Deadline:</strong> ${task.deadline.year}-${task.deadline.month}-${task.deadline.day}</p>
                            <button class="btn btn-primary btn-edit" data-id="${task.id}">Edit</button>
                            <button class="btn btn-danger btn-delete" data-id="${task.id}">Delete</button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="mt-5 mb-4">
            <button class="btn btn-success" id="add-task-button">Add New Task</button>
        </div>

        <div id="calendar"></div>
    </div>

    <jsp:include page="/common/editAddModal.jsp" />
    <script src="static/js/tasks.js"></script>
</body>
</html>
