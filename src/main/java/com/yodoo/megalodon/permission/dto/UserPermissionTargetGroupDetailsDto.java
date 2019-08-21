package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * 用户管理目标集团
 * @Author houzhen
 * @Date 15:22 2019/8/5
**/
public class UserPermissionTargetGroupDetailsDto extends BaseDto {

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

    public UserPermissionTargetGroupDetailsDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetGroupDetailsDto setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getTargetGroupId() {
        return targetGroupId;
    }

    public UserPermissionTargetGroupDetailsDto setTargetGroupId(Integer targetGroupId) {
        this.targetGroupId = targetGroupId;
        return this;
    }
}
