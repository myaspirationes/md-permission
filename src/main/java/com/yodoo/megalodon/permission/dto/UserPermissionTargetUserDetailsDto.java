package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Description ：用户管理目标用户
 * @Author ：jinjun_luo
 * @Date ： 2019/8/7 0007
 */
public class UserPermissionTargetUserDetailsDto extends BaseDto {

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

    public UserPermissionTargetUserDetailsDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetUserDetailsDto setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public UserPermissionTargetUserDetailsDto setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
        return this;
    }
}
