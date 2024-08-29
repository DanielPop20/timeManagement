package com.app.servlet;

import com.app.common.Constants;
import com.app.model.Task;
import com.app.model.User;
import com.app.service.TaskService;
import com.app.util.JsonResponseUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.taskService = new TaskService();
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = getUserFromSession(session);

        if (user != null) {
            String action = req.getParameter("action");
            switch (action) {
                case Constants.ServletConstants.ACTION_FETCH_ALL:
                    handleFetchAllTasks(user.getId(), resp);
                    break;
                case Constants.ServletConstants.ACTION_EDIT:
                    handleEditTask(req, resp);
                    break;
                case Constants.ServletConstants.ACTION_ALL_CATEGORIES:
                    handleFetchAllCategories(resp);
                    break;
                default:
                    JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, Constants.ServletConstants.ERROR_INVALID_ACTION);
                    break;
            }
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, Constants.ServletConstants.ERROR_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = getUserFromSession(session);

        if (user != null) {
            String action = req.getParameter("action");

            try {
                switch (action) {
                    case Constants.ServletConstants.ACTION_ADD:
                        handleAddTask(req, user.getId(), resp);
                        break;
                    case Constants.ServletConstants.ACTION_DELETE:
                        handleDeleteTask(req, resp);
                        break;
                    case Constants.ServletConstants.ACTION_UPDATE:
                        handleUpdateTask(req, resp);
                        break;
                    default:
                        JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, Constants.ServletConstants.ERROR_INVALID_ACTION);
                        break;
                }
            } catch (DateTimeParseException e) {
                JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, Constants.ServletConstants.ERROR_INVALID_DATE_FORMAT);
            } catch (Exception e) {
                JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_PROCESSING_REQUEST);
            }
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, Constants.ServletConstants.ERROR_UNAUTHORIZED);
        }
    }

    private void handleFetchAllTasks(int userId, HttpServletResponse resp) throws IOException {
        try {
            List<Task> tasks = taskService.getTasksByUser(userId);
            JsonResponseUtil.writeSuccessResponse(resp, gson.toJson(tasks));
        } catch (Exception e) {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_RETRIEVING_TASKS);
        }
    }

    private void handleFetchAllCategories(HttpServletResponse resp) throws IOException {
        try {
            List<String> categories = taskService.getTaskCategories();
            JsonResponseUtil.writeSuccessResponse(resp, gson.toJson(categories));
        } catch (Exception e) {
            log.error(Constants.ServletConstants.ERROR_RETRIEVING_TASKS, e);
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_RETRIEVING_TASKS);
        }
    }

    private void handleAddTask(HttpServletRequest req, int userId, HttpServletResponse resp) throws IOException {
        try {
            Task task = createTaskFromRequest(req, userId);
            boolean result = taskService.addTask(task);
            if (result) {
                JsonResponseUtil.writeSuccessResponse(resp, Constants.ServletConstants.SUCCESS_STATUS);
            } else {
                log.error(Constants.ServletConstants.ERROR_ADDING_TASK);
                JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_ADDING_TASK);
            }
        } catch (Exception e) {
            log.error(Constants.ServletConstants.ERROR_ADDING_TASK, e);
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_ADDING_TASK);
        }
    }

    private void handleDeleteTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int taskId = Integer.parseInt(req.getParameter("taskId"));
        boolean isDeleted = taskService.deleteTask(taskId);
        if (isDeleted) {
            JsonResponseUtil.writeSuccessResponse(resp, Constants.ServletConstants.SUCCESS_STATUS);
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_DELETING_TASK);
        }
    }

    private void handleUpdateTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int taskId = Integer.parseInt(req.getParameter("taskId"));
        Task updatedTask = createTaskFromRequest(req, -1);
        updatedTask.setId(taskId);
        boolean isUpdated = taskService.updateTask(updatedTask);
        if (isUpdated) {
            JsonResponseUtil.writeSuccessResponse(resp, Constants.ServletConstants.SUCCESS_STATUS);
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_UPDATING_TASK);
        }
    }

    private void handleEditTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int taskId = Integer.parseInt(req.getParameter("taskId"));
        Task task = taskService.getTaskById(taskId);
        if (task != null) {
            JsonResponseUtil.writeSuccessResponse(resp, gson.toJson(task));
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, Constants.ServletConstants.ERROR_TASK_NOT_FOUND);
        }
    }

    private Task createTaskFromRequest(HttpServletRequest req, int userId) {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String category = req.getParameter("category");
        LocalDate deadline = LocalDate.parse(req.getParameter("deadline"));
        return new Task(0, title, description, category, deadline, userId);
    }

    private User getUserFromSession(HttpSession session) {
        return Optional.ofNullable(session)
                .map(s -> (User) s.getAttribute("user"))
                .orElse(null);
    }
}
