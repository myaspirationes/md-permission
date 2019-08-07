package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 用户管理用户组权限表
 */
public class UserPermissionTargetUserGroupDetails extends BaseEntity {

    /**
     * 用户组id
     */
    private Integer userGroupId;

    /**
     * 用户权限组id
     */
    private Integer userPermissionId;

    public UserPermissionTargetUserGroupDetails() {
    }

    public UserPermissionTargetUserGroupDetails(Integer userGroupId, Integer userPermissionId) {
        this.userGroupId = userGroupId;
        this.userPermissionId = userPermissionId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

}