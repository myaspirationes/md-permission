package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * 权限
 * @Author houzhen
 * @Date 15:22 2019/8/5
**/
public class PermissionDto extends BaseDto {

    /**
     * 权限code
     **/
    @ApiModelProperty(value = "权限code", required = true, example = "permission_code", position = 1)
    private String permissionCode;

    /**
     * 权限名称
     **/
    @ApiModelProperty(value = "权限名称", required = true, example = "permission_name", position = 2)
    private String permissionName;

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
