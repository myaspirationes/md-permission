package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 目标用户表
 */
public class UserPermissionTargetUserDetails extends BaseEntity {

    /**
     * 用户id
     **/
    private Integer userId;

    /**
     * 用户权限id
     **/
    private Integer userPermissionId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

}