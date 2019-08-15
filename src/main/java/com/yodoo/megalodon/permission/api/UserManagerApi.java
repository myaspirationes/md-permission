package com.yodoo.megalodon.permission.api;

import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.UserDto;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import com.yodoo.megalodon.permission.entity.User;
import com.yodoo.megalodon.permission.service.UserGroupService;
import com.yodoo.megalodon.permission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

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
     * 1、添加用户可以选择用户所属的用户组：Set<Integer> userGroupIds
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUser(UserDto userDto){
        return userService.addUser(userDto);
    }

    /**
     * 更新用户：
     * 1、更新用户可以更新用户所属的用户组：Set<Integer> userGroupIds
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
     * 通过账号查询
     * @param account
     * @return
     */
    public User getUserByAccount(String account){
        return userService.getUserByAccount(account);
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
     * 1、维护用户组与条件关系表
     * 2、维护用户组与权限组关系表
     * 3、用户组表
     *
     * @param userGroupDto
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUserGroup(UserGroupDto userGroupDto){
        return userGroupService.addUserGroup(userGroupDto);
    }

    /**
     * 更新用户组：
     * 1、维护用户组与条件关系表
     * 2、维护用户组与权限组关系表
     * 3、用户组表
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer editUserGroup(UserGroupDto userGroupDto) {
        return userGroupService.editUserGroup(userGroupDto);
    }

    /**
     * 删除用户组
     * 1、删除用户组条件
     * 2、删除用户权限
     * 3、用户管理用户组权限表
     * 4、用户组权限组关系
     * 5、删除用户组
     * @param userGroupId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer deleteUserGroup(Integer userGroupId){
        return userGroupService.deleteUserGroup(userGroupId);
    }

    /**
     * 查询所有用户组信息
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public List<UserGroupDto> getUserGroupAll(){
        return userGroupService.getUserGroupAll();
    }

    /**
     * 用户组批处理
     * 1、维护用户与用户组关系表
     * 2、维护用户权限表
     * 3、维护用户权限与用户组关系表
     * @param userGroupId
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public void userGroupBatchProcessing(Integer userGroupId){
        userGroupService.userGroupBatchProcessing(userGroupId);
    }
}