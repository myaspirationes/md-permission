package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：用户管理公司权限表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionTargetCompanyDetails extends BaseEntity {

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

    public UserPermissionTargetCompanyDetails setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetCompanyDetails setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getTargetCompanyId() {
        return targetCompanyId;
    }

    public UserPermissionTargetCompanyDetails setTargetCompanyId(Integer targetCompanyId) {
        this.targetCompanyId = targetCompanyId;
        return this;
    }
}