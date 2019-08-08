package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Description ：用户组权限组关系
 * @Author ：jinjun_luo
 * @Date ： 2019/8/7 0007
 */
public class UserGroupPermissionDetailsDto extends BaseDto {

    /**
     * 用户组id
     */
    private Integer userGroupId;

    /**
     * 权限组id
     */
    private Integer permissionGroupId;

    /**
     * 权限组
     */
    private PermissionGroupDto permissionGroupDto;

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public UserGroupPermissionDetailsDto setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
        return this;
    }

    public Integer getPermissionGroupId() {
        return permissionGroupId;
    }

    public UserGroupPermissionDetailsDto setPermissionGroupId(Integer permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
        return this;
    }

    public PermissionGroupDto getPermissionGroupDto() {
        return permissionGroupDto;
    }

    public UserGroupPermissionDetailsDto setPermissionGroupDto(PermissionGroupDto permissionGroupDto) {
        this.permissionGroupDto = permissionGroupDto;
        return this;
    }
}
