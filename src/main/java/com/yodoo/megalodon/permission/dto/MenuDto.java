package com.yodoo.megalodon.permission.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.megalodon.permission.common.BaseDto;

import java.util.List;

/**
 * @Author houzhen
 * @Date 16:00 2019/7/3
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDto extends BaseDto {

    /**
     * 父类id
     **/
    private Integer parentId;

    /**
     * 菜单code
     **/
    private String menuCode;

    /**
     * 菜单名称
     **/
    private String menuName;

    /**
     * 菜单目标
     **/
    private String menuTarget;

    /**
     * 菜单顺序
     **/
    private String orderNo;

    /**
     * 子菜单
     **/
    private List<MenuDto> children;

    /**
     * 权限id集合
     * @Author houzhen
     * @Date 13:47 2019/8/7
    **/
    private List<Integer> permissionIdList;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuTarget() {
        return menuTarget;
    }

    public void setMenuTarget(String menuTarget) {
        this.menuTarget = menuTarget;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<MenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDto> children) {
        this.children = children;
    }

    public List<Integer> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<Integer> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }
}
