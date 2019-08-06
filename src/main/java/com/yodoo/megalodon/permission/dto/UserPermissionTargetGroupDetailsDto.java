package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * 权限
 * @Author houzhen
 * @Date 15:22 2019/8/5
**/
public class UserPermissionTargetGroupDetailsDto extends BaseDto {

    /**
     * 用户权限id
     **/
    private Integer userPermissionId;

    /**
     * 集团id
     **/
    private Integer groupId;

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
