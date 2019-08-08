package com.yodoo.megalodon.permission.api;

import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.*;
import com.yodoo.megalodon.permission.service.PermissionGroupService;
import com.yodoo.megalodon.permission.service.PermissionService;
import com.yodoo.megalodon.permission.service.UserPermissionDetailsService;
import com.yodoo.megalodon.permission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description ：权限管理api
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
@Component
public class PermissionManagerApi {

    @Autowired
    public UserPermissionDetailsService userPermissionDetailsService;

    @Autowired
    public UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionGroupService permissionGroupService;

    /**
     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list
     * @param userId
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ActionPermissionInUserListDto actionPermissionInUserList(Integer userId){
        return userPermissionDetailsService.actionPermissionInUserList(userId);
    }

    /**
     * 根据用户id查询已管理的目标集团
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getTargetGroupsByUserId(Integer userId){
        return userService.getTargetGroupsByUserId(userId);
    }

    /**
     * 通过用户id查询可管理的目标集团
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getAvailableGroupsByUserId(Integer userId){
        return userService.getAvailableGroupsByUserId(userId);
    }

    /**
     * 通过用户id查询已管理的目标公司
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<CompanyDto> getUserManageTargetCompanyListByUserId(Integer userId){
        return userService.getUserManageTargetCompanyListByUserId(userId);
    }

    /**
     * 通过用户id查询可管理的目标公司
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<CompanyDto> getAvailableUserManageTargetCompanyListByUserId(Integer userId){
        return userService.getAvailableUserManageTargetCompanyListByUserId(userId);
    }

    /**
     * 通过用户id查询已管理的目标用户
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<UserDto> selectUserManageTargetUserListByUserId(Integer userId){
        return userService.selectUserManageTargetUserListByUserId(userId);
    }

    /**
     * 通过用户id查询可管理的目标用户
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<UserDto> getAvailableUserManageTargetUserListByUserId(Integer userId){
        return userService.getAvailableUserManageTargetUserListByUserId(userId);
    }

    /**
     * 条件分页查询权限列表
     * @param permissionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PageInfoDto<PermissionDto> queryPermissionList(PermissionDto permissionDto){
        return permissionService.queryPermissionList(permissionDto);
    }

    /**
     * 添加权限
     * @param permissionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addPermission(PermissionDto permissionDto) {
       return permissionService.addPermission(permissionDto);
    }

    /**
     * 修改权限
     * @param permissionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer updatePermission(PermissionDto permissionDto) {
        return permissionService.updatePermission(permissionDto);
    }

    /**
     * 根据userId查询权限
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<PermissionDto> getPermissionByUserId(Integer userId) {
        return permissionService.getPermissionByUserId(userId);
    }

    /**
     * 条件分页查询权限组列表
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PageInfoDto<PermissionGroupDto> queryPermissionGroupList(PermissionGroupDto permissionGroupDto){
       return permissionGroupService.queryPermissionGroupList(permissionGroupDto);
    }

    /**
     * 添加权限组
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addPermissionGroup(PermissionGroupDto permissionGroupDto) {
        return permissionGroupService.addPermissionGroup(permissionGroupDto);
    }

    /**
     * 更新权限组
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer editPermissionGroup(PermissionGroupDto permissionGroupDto) {
        return permissionGroupService.editPermissionGroup(permissionGroupDto);
    }

    /**
     * 删除权限组 TODO
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deletePermissionGroup(Integer id) {
        return permissionGroupService.deletePermissionGroup(id);
    }

    /**
     * 查询权限组详情
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PermissionGroupDto getPermissionGroupDetails(Integer id) {
        return permissionGroupService.getPermissionGroupDetails(id);
    }
}