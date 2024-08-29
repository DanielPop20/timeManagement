package com.app.servlet;

import com.app.common.Constants;
import com.app.model.User;
import com.app.service.UserService;
import com.app.util.JsonResponseUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        switch (action) {
            case Constants.ServletConstants.ACTION_LOGIN:
                handleLogin(req, resp);
                break;
            case Constants.ServletConstants.ACTION_REGISTER:
                handleRegister(req, resp);
                break;
            case Constants.ServletConstants.ACTION_LOGOUT:
                handleLogout(req, resp);
                break;
            default:
                JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, Constants.ServletConstants.ERROR_INVALID_ACTION);
                break;
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.authenticateUser(username, password);
        if (Objects.nonNull(user)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            JsonResponseUtil.writeSuccessResponse(resp, gson.toJson(new ResponseData(Constants.Pages.DASHBOARD_JSP)));
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, Constants.ServletConstants.ERROR_INVALID_CREDENTIALS);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        boolean success = userService.registerUser(username, password, email);
        if (success) {
            JsonResponseUtil.writeSuccessResponse(resp, gson.toJson(new ResponseData(Constants.Pages.LOGIN_JSP + "?success=true")));
        } else {
            JsonResponseUtil.writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ServletConstants.ERROR_REGISTERING_USER);
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        JsonResponseUtil.writeSuccessResponse(resp, gson.toJson(new ResponseData(Constants.Pages.LOGIN_JSP + "?success=true")));
    }

    @Getter
    @AllArgsConstructor
    private static class ResponseData {
        private String redirect;
    }
}
