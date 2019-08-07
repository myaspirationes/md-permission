package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：权限组明细表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class PermissionGroupDetails extends BaseEntity {

    /**
     * 权限组id
     **/
    private Integer permissionGroupId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    public PermissionGroupDetails() {
    }

    public PermissionGroupDetails(Integer permissionGroupId, Integer permissionId) {
        this.permissionGroupId = permissionGroupId;
        this.permissionId = permissionId;
    }

    public Integer getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(Integer permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}