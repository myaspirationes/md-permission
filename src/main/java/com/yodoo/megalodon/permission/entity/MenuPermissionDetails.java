package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 菜单权限关系
 */
public class MenuPermissionDetails extends BaseEntity {

    /**
     * 菜单id
     **/
    private Integer menuId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}