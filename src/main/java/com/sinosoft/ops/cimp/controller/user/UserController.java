package com.sinosoft.ops.cimp.controller.user;


import com.sinosoft.ops.cimp.config.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.config.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.mapper.user.RoleViewModelMapper;
import com.sinosoft.ops.cimp.mapper.user.UserViewModelMapper;
import com.sinosoft.ops.cimp.service.user.UserRoleService;
import com.sinosoft.ops.cimp.service.user.UserService;
import com.sinosoft.ops.cimp.util.CachePackage.UserCacheManager;
import com.sinosoft.ops.cimp.util.HttpUtils;
import com.sinosoft.ops.cimp.util.PasswordEncoderHelper;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.*;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import com.sinosoft.ops.cimp.vo.from.user.UserSearchViewModel;
import com.sinosoft.ops.cimp.vo.to.user.UserViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SystemLimitsApiGroup
@Api(description = "用户部分接口")
@RestController
@RequestMapping("/user/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;


    @SuppressWarnings("all")
    @ApiOperation(value = "用户登录接口", notes = "本接口登录后会自动写入cookie")
    @ApiParam(value = "登录参数", required = true)
    @PostMapping("/login")
    public ResponseEntity<UserViewModel> login(@Valid @RequestBody LoginViewModel model) throws BusinessException {
        User user = userService.findByLoginName(model.getUserName());
        if (user != null) {
            if ("1".equals(user.getLocked())) {
                return fail("当前用户被锁定，无法登陆！");
            }
            String newPassword = PasswordEncoderHelper.decryptByDES(user.getLoginPassword());
            if (model.getPassWord().equals(newPassword)) {
                SecurityUtils.getSubject().Login(model.getUserName());

                user.setLastLoginIP(HttpUtils.getIpAddr());
                user.setLastLoginTime(new Date());
                userService.modifyUser(user);

                UserViewModel model2 = UserViewModelMapper.INSTANCE.UserToUserViewModel(user);

                Object statisticsViewModel;
                //得到用户拥有的角色
                List<Role> roleList = userRoleService.getRolesByUserId(model2.getId());
                List<RoleViewModel> roleViewModelList = roleList.stream().map(role -> RoleViewModelMapper.INSTANCE.roleToRoleViewModel(role)).collect(Collectors.toList());
                model2.setRoleViewModelList(roleViewModelList);
                //得到用户登录token
                String token = UserCacheManager.getToken(model.getUserName());
                model2.setAccessToken(token);

                //判断用户单位是否含有下级单位权限。用户默认选择单位情况。
                boolean haveChildrenOrg = userService.isHaveChildrenOrg(user.getOrganizationId());
                model2.setHaveChildrenOrg(haveChildrenOrg);

                return ok(model2);
            }
            return fail("密码不正确！");
        }
        return fail("用户名或密码错误！");
    }

    @ApiOperation(value = "获取当前用户信息接口", notes = "本接口获取用户的基本信息2")
    @PostMapping("/getUserInfo")
    @RequiresAuthentication
    public ResponseEntity<UserViewModel> getUserInfo() throws BusinessException {
        User user = SecurityUtils.getSubject().getCurrentUser();
        if (user == null) {
            return fail("无法获取到当前用户的信息！");
        }
        UserViewModel model = UserViewModelMapper.INSTANCE.UserToUserViewModel(user);

        String token = UserCacheManager.getToken(user.getLoginName());
        model.setAccessToken(token);

        return ok(model);
    }

    @ApiOperation(value = "修改用户-获取用户信息接口", notes = "本接口获取用户的基本信息2")
    @PostMapping("/getUserById")
    @RequiresAuthentication
    public ResponseEntity<UserViewModel> getById(String userId) throws BusinessException {
        User user = userService.getById(userId);
        if (user == null) {
            return fail("未找到该ID下的用户！");
        }
        UserViewModel model = UserViewModelMapper.INSTANCE.UserToUserViewModel(user);

        return ok(model);
    }


    @ApiOperation(value = "用户列表")
    @PostMapping("/findByPageData")
    @RequiresAuthentication
    //@RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<PaginationViewModel<UserViewModel>> findByPageData(@RequestBody UserSearchViewModel userSearchViewModel) throws BusinessException {
        PaginationViewModel<UserViewModel> userViewModelLst = userService.findByPageData(userSearchViewModel);
        return ok(userViewModelLst);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/addUser")
    @RequiresAuthentication
    //@RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<String> addUser(@Valid @RequestBody UserAddViewModel userAddViewModel) throws BusinessException {
        List<String> roleCodeList = userAddViewModel.getRoleCodeList();
        User user = UserViewModelMapper.INSTANCE.UserAddViewModelToUser(userAddViewModel);

        boolean flag = userService.checkIsExist(user.getLoginName());
        if (!flag) return fail("用户名已经存在！");
        boolean flag2 = userService.addUser(user, roleCodeList);
        if (flag2) return ok("操作成功");

        return fail("操作异常!");
    }


    @ApiOperation(value = "修改用户")
    @PostMapping("/modifyUser")
    @RequiresAuthentication
    //@RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<String> modifyUser(@Valid @RequestBody UserModifyViewModel userModifyViewModel) throws BusinessException {
        User oldUser = userService.getById(userModifyViewModel.getId());
        List<String> roleCodeList = userModifyViewModel.getRoleCodeList();
        if (oldUser != null) {
            UserViewModelMapper.INSTANCE.userToUserModifyViewModel(oldUser, userModifyViewModel);
            User user = UserViewModelMapper.INSTANCE.userModifyViewModelToUser(userModifyViewModel);
            boolean flag = userService.modifyUser(user, roleCodeList);
            if (flag) {
                return ok("修改成功！");
            }
        }
        return fail("修改异常或者用户名已存在!");
    }

    @ApiOperation(value = "修改用户密码 ")
    @PostMapping("/modifyUserPassword")
    @RequiresAuthentication
    public ResponseEntity<String> modifyUserPassword(@Valid @RequestBody UserModifyPasswordVO passwordVO) throws BusinessException {
        //0修改成功；1未找到当前用户；2原始密码不正确
        if (StringUtils.isEmpty(passwordVO.getId())) {
            return fail("id不能为空！");
        }
        if (StringUtils.isEmpty(passwordVO.getOldPassword())) {
            return fail("oldPassword不能为空！");
        }
        if (StringUtils.isEmpty(passwordVO.getNewPassword())) {
            return fail("newPassword不能为空！");
        }

        String str = userService.modifyUserPassword(passwordVO);

        if ("0".equals(str)) {
            return ok("修改成功！");
        } else if ("1".equals(str)) {
            return fail("未找到当前用户，修改失败！");
        } else if ("2".equals(str)) {
            return fail("原始密码不正确,修改失败！");
        } else {
            return fail("未知返回，请联系管理员！");
        }
    }

    @ApiOperation(value = "修改用户联系方式")
    @PostMapping("/modifyUserContact")
    @RequiresAuthentication
    public ResponseEntity<UserModifyContactVO> modifyUserContact(@Valid @RequestBody UserModifyContactVO contactVO) throws BusinessException {
        if (StringUtils.isEmpty(contactVO.getId())) {
            return fail("id不能为空！");
        }
        UserModifyContactVO userModifyContactVO = userService.modifyUserContact(contactVO);
        return ok(userModifyContactVO);
    }


    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "删除用户")
    @PostMapping(value = "/deleteById")
    @RequiresAuthentication
    public ResponseEntity<String> deleteById(String userId) throws BusinessException {
        boolean flag = userService.deleteById(userId);
        if (flag) return ok("操作成功!");
        return fail("操作异常！");
    }

    /**
     * 登出账号
     *
     * @return
     */
    @ApiOperation(value = "登出账号")
    @PostMapping(value = "/loginOut")
    @RequiresAuthentication
    public ResponseEntity<String> loginOut() throws BusinessException {
        SecurityUtils.getSubject().LoginOut();
        return ok("登出成功！");
    }

    @ApiOperation(value ="重置密码")
    @PostMapping(value = "/resetPassword")
    @RequiresAuthentication
    public ResponseEntity<String> resetPassword(String userId,
                                        String password) throws BusinessException {
        boolean flag = userService.resetPassword(userId, password);
        if (flag) return ok("重置密码成功!");
        return ok("重置密码异常！");
    }

}
