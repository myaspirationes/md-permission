package com.yodoo.megalodon.permission.api;

import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.UserDto;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import com.yodoo.megalodon.permission.service.UserGroupService;
import com.yodoo.megalodon.permission.service.UserPermissionDetailsService;
import com.yodoo.megalodon.permission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * @Description ：用户管理
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
@Component
public class UserManagerApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     *  条件分页查询用户列表:
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public PageInfoDto<UserDto> queryUserList(UserDto userDto){
        return userService.queryUserList(userDto);
    }

    /**
     * 添加用户：
     * 1、添加用户可以设定用户所拥有的权限： Set<Integer> permissionIds
     * 2、添加用户可以选择用户所属的用户组：Set<Integer> userGroupIds
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUser(UserDto userDto){
        return userService.addUser(userDto);
    }

    /**
     * 更新用户：
     * 1、更新用户可以更新用户所拥有的权限：Set<Integer> permissionIds
     * 2、更新用户可以更新用户所属的用户组：Set<Integer> userGroupIds
     * 注意：用户所拥有的权限和所属的用户组数据要全传进来，后台逻辑
     *
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer editUser(UserDto userDto){
        return userService.editUser(userDto);
    }

    /**
     * 重置密码：yodoo123
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer resetUserPassword(Integer userId){
        return userService.resetUserPassword(userId);
    }

    /**
     * 启用和停用:
     * @param userId
     * @param status ：0：启用 1：停用
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer updateUserStatus(Integer userId, Integer status){
        return userService.updateUserStatus(userId, status);
    }

    /**
     * 条件分页查询用户组
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public PageInfoDto<UserGroupDto> queryUserGroupList(UserGroupDto userGroupDto){
        return userGroupService.queryUserGroupList(userGroupDto);
    }

    /**
     * 添加用户组:
     * 1、用户组权限表
     * 2、用户管理用户组权限表
     * 3、用户组条件表
     *
     * @param userGroupDto
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUserGroup(UserGroupDto userGroupDto){
        return userGroupService.addUserGroup(userGroupDto);
    }

    /**
     * 更新用户组：
     * 1、用户组权限表
     * 2、用户管理用户组权限表
     * 3、用户组条件表
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer editUserGroup(UserGroupDto userGroupDto) {
        return userGroupService.editUserGroup(userGroupDto);
    }

}
