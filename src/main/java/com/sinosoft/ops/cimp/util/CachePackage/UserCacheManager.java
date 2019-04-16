package com.sinosoft.ops.cimp.util.CachePackage;



import com.sinosoft.ops.cimp.entity.sys.user.QUserRole;
import com.sinosoft.ops.cimp.entity.sys.user.User;
import com.sinosoft.ops.cimp.entity.sys.user.UserRole;
import com.sinosoft.ops.cimp.repository.user.UserRoleRepository;
import com.sinosoft.ops.cimp.service.user.UserService;
import com.sinosoft.ops.cimp.util.Cache;
import com.sinosoft.ops.cimp.util.CacheManager;
import com.sinosoft.ops.cimp.util.SpringContextUtils;
import com.sinosoft.ops.cimp.util.TokenUtils;
import org.apache.commons.collections.IteratorUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserCacheManager {
    private static final String X_USERINFO = "X-USERINFO";
    private static final String X_USERROLE = "X-USERROLE";
    private static final String X_TOKEN = "X-TOKEN";

    /**
     * 获取当前用户信息
     *
     * @param LoginName 用户名
     * @return
     */
    public synchronized static User getUserInfo(String LoginName) {
        Cache cache = CacheManager.getCacheInfo(LoginName + X_USERINFO);
        if (cache == null) {
            UserService userService = SpringContextUtils.getBean(UserService.class);
            User loginUser = userService.findByLoginName(LoginName);
            if (loginUser != null) {
                setUserInfo(LoginName, loginUser);
                return loginUser;
            }
            return null;
        }
        Object obj = cache.getValue();
        if (obj instanceof User) {
            return (User) obj;
        } else {
            return null;
        }
    }

    /**
     * 设置用户缓存
     *
     * @param LoginName 用户名
     * @return
     */
    public synchronized static void setUserInfo(String LoginName, User user) {
        Cache usercache = new Cache();
        usercache.setKey("user");
        usercache.setValue(user);
        CacheManager.putCache(LoginName + X_USERINFO, usercache);
    }

    public synchronized static String getToken(String LoginName) {
        Cache cache = CacheManager.getCacheInfo(LoginName + X_TOKEN);
        if (cache == null) {
            String token = TokenUtils.createJwtToken(LoginName);
            setToken(LoginName, token);
            return token;
        }
        Object obj = cache.getValue();
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return null;
        }
    }

    public synchronized static void setToken(String LoginName, String token) {
        Cache tokencache = new Cache();
        tokencache.setKey("token");
        tokencache.setValue(token);
        CacheManager.putCache(LoginName + X_TOKEN, tokencache);
    }

    /**
     * 获取用户角色缓存
     *
     * @param LoginName 用户名
     * @return
     */
    public synchronized static List<UserRole> getUserRole(String LoginName) {
        Cache cache = CacheManager.getCacheInfo(LoginName + X_USERROLE);
        if (cache == null) {
            UserRoleRepository userService = SpringContextUtils.getBean(UserRoleRepository.class);
            User loginUser = getUserInfo(LoginName);
            if (loginUser != null) {
                QUserRole qUserRole = QUserRole.userRole;
                Iterator<UserRole> iterator = userService.findAll(qUserRole.userId.eq(loginUser.getId())).iterator();
                List<UserRole> list = IteratorUtils.toList(iterator);
                setUserRole(LoginName, list);
                return list;
            }
            return new ArrayList<UserRole>();
        }
        Object obj = cache.getValue();
        if (obj instanceof List) {
            return (List<UserRole>) obj;
        } else {
            return new ArrayList<UserRole>();
        }
    }

    /**
     * 设置用户角色缓存
     *
     * @param LoginName 用户名
     * @return
     */
    public synchronized static void setUserRole(String LoginName, List<UserRole> userRoles) {
        Cache userrolecache = new Cache();
        userrolecache.setKey("userrole");
        userrolecache.setValue(userRoles);
        CacheManager.putCache(LoginName + X_USERROLE, userrolecache);
    }

    /**
     * 清空缓存
     *
     * @param loginName 用户名
     * @return
     */
    public synchronized static void clearUser(String loginName) {
        CacheManager.clearOnly(loginName + X_USERINFO);
        CacheManager.clearOnly(loginName + X_USERROLE);
    }

}
