package com.sinosoft.ops.cimp.service.user;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.vo.from.user.UserModifyContactVO;
import com.sinosoft.ops.cimp.vo.from.user.UserModifyPasswordVO;
import com.sinosoft.ops.cimp.vo.from.user.UserSearchViewModel;
import com.sinosoft.ops.cimp.vo.to.user.UserViewModel;

import java.util.List;

public interface UserService {

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return
     */
    boolean addUser(User user, List<String> roleCodeList);

    boolean checkIsExist(String loginName);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return
     */
    boolean modifyUser(User user, List<String>... roleList);

    /**
     * 修改用户联系方式
     */
    UserModifyContactVO modifyUserContact(UserModifyContactVO contactVO);

    /**
     * 修改用户密码
     */
    String modifyUserPassword(UserModifyPasswordVO passwordVO);

    /**
     * 根据id查询用户信息
     *
     * @param userId 登陆名
     * @return 用户信息
     */
    User getById(String userId);

    /**
     * 根据登陆名查询用户
     *
     * @param loginName 登陆名
     * @return 用户信息
     */
    User findByLoginName(String loginName);


    /**
     * 查询用户列表
     *
     * @return
     */
    PaginationViewModel<UserViewModel> findByPageData(UserSearchViewModel userSearchViewModel);



    /**
     * 根据用户ID删除
     *
     * @param userId
     * @return
     */
    boolean deleteById(String userId);

    /**
     * 查询该账号下同单位账号ID
     */
    List<String> findSameOrgId();

    /**
     * 当前单位是否含有下级
     *
     * @return
     */
    boolean isHaveChildrenOrg(String orgId);


    boolean resetPassword(String userId, String password);

    /**
     * 根据用户id查询权限
     * @param userId
     * @return
     */
    List<Role> findUserRole(String userId);
}
