package com.app.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonResponseUtil {

    public static void writeSuccessResponse(HttpServletResponse resp, String json) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    public static void writeErrorResponse(HttpServletResponse resp, int statusCode, String errorMessage) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = String.format("{\"error\":\"%s\"}", errorMessage);
        resp.getWriter().write(json);
    }
}
