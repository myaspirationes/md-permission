package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：用户组权限表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserGroupPermissionDetails extends BaseEntity {

    /**
     * 用户组id
     */
    private Integer userGroupId;

    /**
     * 权限组id
     */
    private Integer permissionGroupId;

    public UserGroupPermissionDetails() {
    }

    public UserGroupPermissionDetails(Integer userGroupId, Integer permissionGroupId) {
        this.userGroupId = userGroupId;
        this.permissionGroupId = permissionGroupId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(Integer permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }

}