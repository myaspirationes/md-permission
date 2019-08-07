package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：权限组表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class PermissionGroup extends BaseEntity {

    /**
     * 权限组code
     **/
    private String groupCode;

    /**
     * 权限组名称
     **/
    private String groupName;

    public PermissionGroup() {
    }

    public PermissionGroup(String groupCode, String groupName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

}