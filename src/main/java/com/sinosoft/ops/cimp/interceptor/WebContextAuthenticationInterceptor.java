package com.sinosoft.ops.cimp.interceptor;


import com.sinosoft.ops.cimp.annotation.Anonymous;
import com.sinosoft.ops.cimp.annotation.Logical;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.RequiresRoles;
import com.sinosoft.ops.cimp.constant.UserConstants;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class WebContextAuthenticationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        Anonymous methodAnonymous = method.getAnnotation(Anonymous.class);
        if (methodAnonymous != null) {
            return true;
        }

        // 判断接口是否需要登录
        RequiresAuthentication methodAnnotation = method.getAnnotation(RequiresAuthentication.class);

        RequiresRoles methodRequiresRoles = method.getAnnotation(RequiresRoles.class);

        if (methodAnnotation != null || methodRequiresRoles != null) {
            User user = SecurityUtils.getSubject().getCurrentUser();
            if (user == null) {
                response.setStatus(401);
                response.getOutputStream().println("{code:401, message:\"no auth,please login!\"}");
                return false;
            }

            if (methodRequiresRoles != null) {
                String[] role = methodRequiresRoles.value();
                Logical logical = methodRequiresRoles.logical();
                List<UserRole> list = SecurityUtils.getSubject().getCurrentUserRole();

                final boolean contains[] = {false};
                list.stream().forEach(x -> {
                    if (ArrayUtils.contains(role, x.getRoleCode())) {
                        contains[0] = true;
                    }
                });
                if (contains[0]) {
                    request.setAttribute(UserConstants.CURRENT_USER, user);
                    return true;
                } else {
                    response.setStatus(401);
                    response.getOutputStream().println("{code:\"401\", message:\"requires roles error!\"}");
                    return false;
                }
            }
            request.setAttribute(UserConstants.CURRENT_USER, user);
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}