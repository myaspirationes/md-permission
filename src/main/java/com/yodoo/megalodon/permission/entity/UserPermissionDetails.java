package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 用户权限表
 */
public class UserPermissionDetails extends BaseEntity {

    /**
     * 用户id
     **/
    private Integer userId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}