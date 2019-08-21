package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：菜单权限关系
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
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

    public MenuPermissionDetails() {
    }

    public MenuPermissionDetails(Integer menuId, Integer permissionId) {
        this.menuId = menuId;
        this.permissionId = permissionId;
    }

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