package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：目标用户表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionTargetUserDetails extends BaseEntity {

    /**
     * 用户权限id
     **/
    private Integer userPermissionId;

    /**
     * 用户id
     **/
    private Integer userId;

    public UserPermissionTargetUserDetails() {
    }

    public UserPermissionTargetUserDetails(Integer userPermissionId, Integer userId) {
        this.userPermissionId = userPermissionId;
        this.userId = userId;
    }

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}