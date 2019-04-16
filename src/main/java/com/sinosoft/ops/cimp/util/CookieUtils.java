package com.sinosoft.ops.cimp.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtils {
    /**
     * 设置cookie
     *
     * @param response 响应对象
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期  以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        if(response == null){
            return;
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 删除登陆cookie
     *
     * @param response 响应对象
     */
    public static void removeLoginCookie(HttpServletResponse response) {
        removeCookie(response, "lgUid");
    }

    /**
     * 删除cookie
     *
     * @param response 响应对象
     * @param name     cookie名字
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 根据名字获取cookie
     *
     * @param request 请求对象
     * @param name    cookie名字
     * @return 若存在对应的Cookie则返回否则返回null
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        return cookieMap.getOrDefault(name, null);
    }


    /**
     * 将cookie封装到Map里面
     *
     * @param request 请求对象
     * @return 若存在Cookie则返回Map（键为cookie名值为Cookie对象）
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
