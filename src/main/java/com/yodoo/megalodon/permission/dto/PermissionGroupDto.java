package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

/**
 * @Description ：权限组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class PermissionGroupDto extends BaseDto {

    /**
     * 权限组code
     **/
    @ApiModelProperty(value = "权限组code", required = true, example = "permisson_group_code", position = 1)
    private String groupCode;

    /**
     * 权限组名称
     **/
    @ApiModelProperty(value = "权限组名称", required = true, example = "permission_group_name", position = 2)
    private String groupName;

    /**
     * 权限ids
     */
    @ApiModelProperty(value = "权限ids", required = true, example = "[1,2]", position = 3)
    private Set<Integer> permissionIds;

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

    public Set<Integer> getPermissionIds() {
        return permissionIds;
    }

    public PermissionGroupDto setPermissionIds(Set<Integer> permissionIds) {
        this.permissionIds = permissionIds;
        return this;
    }
}
