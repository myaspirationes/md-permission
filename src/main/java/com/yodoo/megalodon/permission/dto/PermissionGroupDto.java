package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Description ：权限组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class PermissionGroupDto extends BaseDto {

    /**
     * 权限组code
     **/
    private String groupCode;

    /**
     * 权限组名称
     **/
    private String groupName;

    public PermissionGroupDto() {
    }

    public PermissionGroupDto(String groupCode, String groupName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public PermissionGroupDto setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public PermissionGroupDto setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }
}
