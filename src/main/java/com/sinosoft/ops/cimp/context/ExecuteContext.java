package com.sinosoft.ops.cimp.context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ExecuteContext {
    private static final ThreadLocal<ExecuteContext> LOCAL = new ThreadLocal<>();
    private HttpServletRequest request;
    private HttpServletResponse response;

    private ConcurrentHashMap<String, Object> variables = new ConcurrentHashMap<>();

    // 初始化
    public static void init(HttpServletRequest request, HttpServletResponse response) {
        ExecuteContext executeContext = new ExecuteContext();
        executeContext.request = request;
        executeContext.response = response;
        LOCAL.set(executeContext);
    }

    // 销毁
    public static void destroy() {
        LOCAL.remove();
    }

    // 获取 Request
    private static HttpServletRequest getRequest() {
        return LOCAL.get().request;
    }

    // 获取 Response
    private static HttpServletResponse getResponse() {
        return LOCAL.get().response;
    }

    // 获取 Session
    private static HttpSession getSession() {
        return getRequest().getSession();
    }

    // 获取 Servlet Context
    private static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    // 封装 Request 相关操作
    public static class Request {

        public static HttpServletRequest currentRequest() {
            return getRequest();
        }

        // 将数据放入 Request 中
        public static void put(String key, Object value) {
            getRequest().setAttribute(key, value);
        }

        // 从 Request 中获取数据
        public static Object get(String key) {
            return getRequest().getAttribute(key);
        }

        // 移除 Request 中的数据
        public static void remove(String key) {
            getRequest().removeAttribute(key);
        }

        // 从 Request 中获取所有数据
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<>();
            Enumeration<String> names = getRequest().getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getRequest().getParameter(name));
            }
            return map;
        }
    }

    // 封装 Response 相关操作
    public static class Response {

        public static HttpServletResponse currentResponse() {
            return getResponse();
        }

        // 将数据放入 Response 中
        public static void put(String key, Object value) {
            getResponse().setHeader(key, String.valueOf(value));
        }

        // 从 Response 中获取数据
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getResponse().getHeader(key);
        }

        // 从 Response 中获取所有数据
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<>();
            for (String name : getResponse().getHeaderNames()) {
                map.put(name, getResponse().getHeader(name));
            }
            return map;
        }
    }

    // 封装 Session 相关操作
    public static class Session {

        // 将数据放入 Session 中
        public static void put(String key, Object value) {
            getSession().setAttribute(key, value);
        }

        // 从 Session 中获取数据
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getSession().getAttribute(key);
        }

        // 移除 Session 中的数据
        public static void remove(String key) {
            getSession().removeAttribute(key);
        }

        // 从 Session 中获取所有数据
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<>();
            Enumeration<String> names = getSession().getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getSession().getAttribute(name));
            }
            return map;
        }

        // 移除 Session Attribute 中所有的数据
        public static void removeAll() {
            getSession().invalidate();
        }
    }

    // 封装 ServletContext 相关操作
    public static class Context {

        // 将数据放入 ServletContext 中
        public static void put(String key, Object value) {
            getServletContext().setAttribute(key, value);
        }

        // 从 ServletContext 中获取数据
        @SuppressWarnings("unchecked")
        public static <T> T get(String key) {
            return (T) getServletContext().getAttribute(key);
        }

        // 移除 ServletContext 中的数据
        public static void remove(String key) {
            getServletContext().removeAttribute(key);
        }

        // 从 ServletContext 中获取所有数据
        public static Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<>();
            Enumeration<String> names = getServletContext().getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getServletContext().getAttribute(name));
            }
            return map;
        }
    }


    public static Object getVariable(String key) {
        return LOCAL.get().variables.get(key);
    }

    public static void putVariable(String key, Object value) {
        LOCAL.get().variables.put(key, value);
    }

    public static void removeVariable(String key) {
        LOCAL.get().variables.remove(key);
    }

    public static Map<String, Object> getAllVariables() {
        return LOCAL.get().variables;
    }

    public static String getCurrentUserId() {
        return "-1";
    }
}
