package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：目标集团表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionTargetGroupDetails extends BaseEntity {

    /**
     * 用户权限id
     **/
    private Integer userPermissionId;

    /**
     * 集团id
     **/
    private Integer groupId;

    public UserPermissionTargetGroupDetails() {
    }

    public UserPermissionTargetGroupDetails(Integer userPermissionId, Integer groupId) {
        this.userPermissionId = userPermissionId;
        this.groupId = groupId;
    }

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