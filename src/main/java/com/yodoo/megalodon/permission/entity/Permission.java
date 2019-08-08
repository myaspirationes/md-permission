package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：权限表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class Permission extends BaseEntity {

    /**
     * 权限code
     **/
    private String permissionCode;

    /**
     * 权限名称
     **/
    private String permissionName;

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode == null ? null : permissionCode.trim();
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }
}