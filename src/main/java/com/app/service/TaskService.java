package com.app.service;

import com.app.dao.CategoriesDao;
import com.app.dao.TaskDao;
import com.app.model.Category;
import com.app.model.Task;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskDao taskDao;
    private final CategoriesDao categoriesDao;

    public TaskService() {
        this.taskDao = new TaskDao();
        this.categoriesDao = new CategoriesDao();
    }

    public boolean addTask(Task task) throws Exception {
        try {
            validateTask(task);
            return taskDao.addTask(task);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Task> getTasksByUser(int userId) {
        return taskDao.getTasksByUser(userId);
    }

    public List<String> getTaskCategories(){
        return categoriesDao.getAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    public Task getTaskById(int taskId) {
        return taskDao.getTaskById(taskId);
    }

    public boolean updateTask(Task task){
        return taskDao.updateTask(task);
    }

    public boolean deleteTask(int taskId) {
        return taskDao.deleteTask(taskId);
    }

    private void validateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (task.getDeadline() == null) {
            throw new IllegalArgumentException("Deadline cannot be null");
        }
    }
}
