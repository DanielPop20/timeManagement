package com.app.dao;

import com.app.model.User;
import com.app.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, password, email, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setObject(4, user.getCreatedAt());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return null;
    }
}
