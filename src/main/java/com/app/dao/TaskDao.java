package com.app.dao;

import com.app.model.Task;
import com.app.util.DBConnectionUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public boolean addTask(Task task) {
        String query = "INSERT INTO tasks (title, description, category, deadline, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getCategory());
            ps.setDate(4, Date.valueOf(task.getDeadline()));
            ps.setInt(5, task.getUserId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setCategory(rs.getString("category"));
                task.setDeadline(rs.getDate("deadline").toLocalDate());
                tasks.add(task);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return tasks;
    }

    public Task getTaskById(int taskId) {
        String query = "SELECT * FROM tasks WHERE id = ?";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setCategory(rs.getString("category"));
                task.setDeadline(rs.getDate("deadline").toLocalDate());
                task.setUserId(rs.getInt("user_id"));
                return task;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return null;
    }

    public boolean updateTask(Task task) {
        String query = "UPDATE tasks SET title = ?, description = ?, category = ?, deadline = ? WHERE id = ?";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getCategory());
            ps.setDate(4, Date.valueOf(task.getDeadline()));
            ps.setInt(5, task.getId());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public boolean deleteTask(int taskId) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, taskId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
