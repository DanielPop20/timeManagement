package com.app.common;

public class Constants {
    public static final String ERROR_QUERY_PARAM = "error=true";

    public static class Pages {
        public static final String DASHBOARD_JSP = "dashboard.jsp";
        public static final String LOGIN_JSP = "login.jsp";
    }

    public static class ServletConstants {
        public static final String ACTION_FETCH_ALL = "fetchAll";
        public static final String ACTION_ADD = "add";
        public static final String ACTION_DELETE = "delete";
        public static final String ACTION_UPDATE = "update";
        public static final String ACTION_EDIT = "edit";
        public static final String ACTION_LOGOUT = "logout";
        public static final String ACTION_LOGIN = "login";
        public static final String ACTION_REGISTER = "register";
        public static final String ACTION_ALL_CATEGORIES = "allCategories";

        public static final String ERROR_INVALID_CREDENTIALS = "Error invalid credentials";
        public static final String ERROR_REGISTERING_USER = "Error registering user";

        public static final String ERROR_INVALID_ACTION = "Invalid action";
        public static final String ERROR_UNAUTHORIZED = "Unauthorized";
        public static final String ERROR_INVALID_DATE_FORMAT = "Invalid date format";
        public static final String ERROR_PROCESSING_REQUEST = "Error processing request";
        public static final String ERROR_RETRIEVING_TASKS = "Error retrieving tasks";
        public static final String ERROR_ADDING_TASK = "Error adding task";
        public static final String ERROR_DELETING_TASK = "Error deleting task";
        public static final String ERROR_UPDATING_TASK = "Error updating task";
        public static final String ERROR_TASK_NOT_FOUND = "Error task not found";

        public static final String SUCCESS_STATUS = "{\"status\":\"success\"}";
    }
}
