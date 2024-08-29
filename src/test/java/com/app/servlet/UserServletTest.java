package com.app.servlet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.app.common.Constants;
import com.app.model.User;
import com.app.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class UserServletTest {

    private UserServlet servlet;
    private UserService mockUserService;
    private static final String EXPECTED_PATTERN = "login.jsp?success\\u003dtrue";

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockUserService = mock(UserService.class);
        Gson gson = new GsonBuilder().create();

        servlet = new UserServlet();

        Field userServiceField = UserServlet.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(servlet, mockUserService);

        Field gsonField = UserServlet.class.getDeclaredField("gson");
        gsonField.setAccessible(true);
        gsonField.set(servlet, gson);
    }

    @Test
    public void testHandleLoginSuccess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("action")).thenReturn(Constants.ServletConstants.ACTION_LOGIN);
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("testPass");

        User mockUser = new User();
        when(mockUserService.authenticateUser("testUser", "testPass")).thenReturn(mockUser);

        when(request.getSession()).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        String jsonResponse = stringWriter.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(Constants.Pages.DASHBOARD_JSP));
    }

    @Test
    public void testHandleLoginFailure() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("action")).thenReturn(Constants.ServletConstants.ACTION_LOGIN);
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("testPass");

        when(mockUserService.authenticateUser("testUser", "testPass")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String jsonResponse = stringWriter.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(Constants.ServletConstants.ERROR_INVALID_CREDENTIALS));
    }

    @Test
    public void testHandleRegisterSuccess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("action")).thenReturn(Constants.ServletConstants.ACTION_REGISTER);
        when(request.getParameter("username")).thenReturn("newUser");
        when(request.getParameter("password")).thenReturn("newPass");
        when(request.getParameter("email")).thenReturn("newUser@example.com");

        when(mockUserService.registerUser("newUser", "newPass", "newUser@example.com")).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        String jsonResponse = stringWriter.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(EXPECTED_PATTERN));
    }

    @Test
    public void testHandleRegisterFailure() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("action")).thenReturn(Constants.ServletConstants.ACTION_REGISTER);
        when(request.getParameter("username")).thenReturn("newUser");
        when(request.getParameter("password")).thenReturn("newPass");
        when(request.getParameter("email")).thenReturn("newUser@example.com");

        when(mockUserService.registerUser("newUser", "newPass", "newUser@example.com")).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        String jsonResponse = stringWriter.toString();
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(Constants.ServletConstants.ERROR_REGISTERING_USER));
    }

    @Test
    public void testHandleLogout() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("action")).thenReturn(Constants.ServletConstants.ACTION_LOGOUT);
        when(request.getSession(false)).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        String jsonResponse = stringWriter.toString();
        verify(session).invalidate();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(EXPECTED_PATTERN));
    }

    @Test
    public void testHandleInvalidAction() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("action")).thenReturn("invalidAction");

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonResponse = stringWriter.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(Constants.ServletConstants.ERROR_INVALID_ACTION));
    }
}
