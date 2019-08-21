package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：目标用户表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionTargetUserDetails extends BaseEntity {

    /**
     * 用户id
     **/
    private Integer userId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    /**
     * 目标用户id
     */
    private Integer targetUserId;

    public Integer getUserId() {
        return userId;
    }

    public UserPermissionTargetUserDetails setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetUserDetails setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public UserPermissionTargetUserDetails setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
        return this;
    }
}