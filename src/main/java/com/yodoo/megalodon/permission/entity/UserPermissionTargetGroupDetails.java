package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：目标集团表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionTargetGroupDetails extends BaseEntity {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    /**
     * 目标集团id
     **/
    private Integer targetGroupId;

    public Integer getUserId() {
        return userId;
    }

    public UserPermissionTargetGroupDetails setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetGroupDetails setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getTargetGroupId() {
        return targetGroupId;
    }

    public UserPermissionTargetGroupDetails setTargetGroupId(Integer targetGroupId) {
        this.targetGroupId = targetGroupId;
        return this;
    }
}