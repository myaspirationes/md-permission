package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 用户管理公司权限表
 */
public class UserPermissionTargetCompanyDetails extends BaseEntity {

    /**
     * 用户权限组id
     **/
    private Integer userPermissionId;

    /**
     * 集团id
     **/
    private Integer companyId;

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}