package com.app.servlet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.app.common.Constants;
import com.app.model.Task;
import com.app.model.User;
import com.app.service.TaskService;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TaskServletTest {

    private TaskServlet servlet;
    private TaskService mockTaskService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockTaskService = mock(TaskService.class);
        Gson gson = new GsonBuilder().create();

        servlet = new TaskServlet();

        Field taskServiceField = TaskServlet.class.getDeclaredField("taskService");
        taskServiceField.setAccessible(true);
        taskServiceField.set(servlet, mockTaskService);

        Field gsonField = TaskServlet.class.getDeclaredField("gson");
        gsonField.setAccessible(true);
        gsonField.set(servlet, gson);
    }

    @Test
    public void testHandleFetchAllTasks() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User mockUser = new User();
        mockUser.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("action")).thenReturn("fetchAll");

        Task task1 = new Task(1, "Task 1", "Description 1", "Category 1", LocalDate.now(), 1);
        Task task2 = new Task(2, "Task 2", "Description 2", "Category 2", LocalDate.now(), 1);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(mockTaskService.getTasksByUser(1)).thenReturn(tasks);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doGet(request, response);

        String jsonResponse = stringWriter.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains("Task 1"));
        assertTrue(jsonResponse.contains("Task 2"));
    }

    @Test
    public void testHandleFetchAllCategories() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User mockUser = new User();
        mockUser.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("action")).thenReturn("allCategories");

        List<String> categories = Arrays.asList("Work", "Personal", "Urgent");
        when(mockTaskService.getTaskCategories()).thenReturn(categories);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doGet(request, response);

        String jsonResponse = stringWriter.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains("Work"));
        assertTrue(jsonResponse.contains("Personal"));
        assertTrue(jsonResponse.contains("Urgent"));
    }

    @Test
    public void testHandleAddTask() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User mockUser = new User();
        mockUser.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("title")).thenReturn("New Task");
        when(request.getParameter("description")).thenReturn("Task Description");
        when(request.getParameter("category")).thenReturn("Work");
        when(request.getParameter("deadline")).thenReturn(LocalDate.now().toString());

        when(mockTaskService.addTask(any(Task.class))).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        assertEquals(Constants.ServletConstants.SUCCESS_STATUS, stringWriter.toString());
    }

    @Test
    public void testHandleUpdateTask() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User mockUser = new User();
        mockUser.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("taskId")).thenReturn("2");
        when(request.getParameter("title")).thenReturn("Updated Task");
        when(request.getParameter("description")).thenReturn("Updated Description");
        when(request.getParameter("category")).thenReturn("Personal");
        when(request.getParameter("deadline")).thenReturn(LocalDate.now().toString());

        when(mockTaskService.updateTask(any(Task.class))).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        assertEquals(Constants.ServletConstants.SUCCESS_STATUS, stringWriter.toString());
    }

    @Test
    public void testHandleDeleteTask() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User mockUser = new User();
        mockUser.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("taskId")).thenReturn("1");

        when(mockTaskService.deleteTask(1)).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter mockWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doPost(request, response);

        assertEquals(Constants.ServletConstants.SUCCESS_STATUS, stringWriter.toString());
    }
}
