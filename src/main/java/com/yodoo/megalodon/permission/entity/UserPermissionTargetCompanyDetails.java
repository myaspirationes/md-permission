package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：用户管理公司权限表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserPermissionTargetCompanyDetails extends BaseEntity {

    /**
     * 用户权限组id
     **/
    private Integer userPermissionId;

    /**
     * 公司id
     **/
    private Integer companyId;

    public UserPermissionTargetCompanyDetails() {
    }

    public UserPermissionTargetCompanyDetails(Integer userPermissionId, Integer companyId) {
        this.userPermissionId = userPermissionId;
        this.companyId = companyId;
    }

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