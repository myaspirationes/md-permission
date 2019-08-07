package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：用户权限表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionDetails extends BaseEntity {

    /**
     * 用户id
     **/
    private Integer userId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    public UserPermissionDetails() {
    }

    public UserPermissionDetails(Integer userId, Integer permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}