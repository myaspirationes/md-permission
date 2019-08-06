package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Author houzhen
 * @Date 9:57 2019/8/6
**/
public class UserPermissionDetailsDto extends BaseDto {

    /**
     * 用户组id
     */
    private Integer userGroupId;

    /**
     * 权限组id
     */
    private Integer permissionGroupId;

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
