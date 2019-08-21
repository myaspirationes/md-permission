package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Description ：用户管理目标公司
 * @Author ：jinjun_luo
 * @Date ： 2019/8/7 0007
 */
public class UserPermissionTargetCompanyDetailsDto extends BaseDto {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 权限id
     */
    private Integer permissionId;

    /**
     * 目标公司id
     */
    private Integer targetCompanyId;

    public Integer getUserId() {
        return userId;
    }

    public UserPermissionTargetCompanyDetailsDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetCompanyDetailsDto setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getTargetCompanyId() {
        return targetCompanyId;
    }

    public UserPermissionTargetCompanyDetailsDto setTargetCompanyId(Integer targetCompanyId) {
        this.targetCompanyId = targetCompanyId;
        return this;
    }
}
