package com.app.service;

import com.app.dao.UserDao;
import com.app.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService() {
        this.userDao = new UserDao();
    }

    public boolean registerUser(String username, String password, String email) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(0, username, hashedPassword, email, LocalDateTime.now());
        return userDao.registerUser(user);
    }

    public User authenticateUser(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
