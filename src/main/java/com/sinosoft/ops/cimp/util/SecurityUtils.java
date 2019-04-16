package com.sinosoft.ops.cimp.util;



import com.sinosoft.ops.cimp.entity.sys.user.User;
import com.sinosoft.ops.cimp.entity.sys.user.UserRole;
import com.sinosoft.ops.cimp.util.CachePackage.UserCacheManager;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import java.util.List;

public class SecurityUtils {
    private static SecurityUtils securityUtil;

    public final static String ACCESS_TOKEN = "accessToken";

    public static SecurityUtils getSubject() {
        if (securityUtil == null) {
            securityUtil = new SecurityUtils();
        }
        return securityUtil;
    }

    /**
     * 当前登录人用户名
     *
     * @return
     */
    public String getCurrentLoginName() {
        if (ExecuteContext.Request.getCurrent() == null) {
            return "";
        }

        String loginName = "";
        String accessToken = ExecuteContext.Request.getCurrent().getHeader(ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken)) {
            Cookie cookie = CookieUtils.getCookieByName(ExecuteContext.Request.getCurrent(), ACCESS_TOKEN);
            if (cookie != null) {
                loginName = cookie.getValue();
            }
        } else {
            loginName = accessToken;
        }

        if (StringUtils.isEmpty(loginName)) {
            return "";
        }

        Claims claims = TokenUtils.parseJWT(loginName);

        return claims.getId();
    }

    /**
     * 当前登录人信息
     *
     * @return
     */
    public User getCurrentUser() {
        return UserCacheManager.getUserInfo(getCurrentLoginName());
    }

    public List<UserRole> getCurrentUserRole() {
        return UserCacheManager.getUserRole(getCurrentLoginName());
    }

    /**
     * 登陆后调用
     *
     * @return
     */
    public void Login(String loginName) {
        UserCacheManager.clearUser(loginName);
        UserCacheManager.getUserInfo(loginName);
        UserCacheManager.getUserRole(loginName);

        String token = TokenUtils.createJwtToken(loginName);
        UserCacheManager.setToken(loginName, token);
        CookieUtils.addCookie(ExecuteContext.Response.getCurrent(), ACCESS_TOKEN, token, 0);
    }

    /**
     * 退出调用
     * @return
     */
    public void LoginOut() {
        UserCacheManager.clearUser(getCurrentLoginName());
        CookieUtils.removeCookie(ExecuteContext.Response.getCurrent(), ACCESS_TOKEN);
    }

}
