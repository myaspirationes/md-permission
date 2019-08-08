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
     * 用户权限id
     **/
    private Integer userPermissionId;

    public Integer getUserId() {
        return userId;
    }

    public UserPermissionTargetUserDetailsDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public UserPermissionTargetUserDetailsDto setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
        return this;
    }
}
