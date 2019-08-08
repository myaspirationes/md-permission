package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Description ：用户管理目标公司
 * @Author ：jinjun_luo
 * @Date ： 2019/8/7 0007
 */
public class UserPermissionTargetCompanyDetailsDto extends BaseDto {

    /**
     * 用户权限组id
     **/
    private Integer userPermissionId;

    /**
     * 公司id
     **/
    private Integer companyId;

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public UserPermissionTargetCompanyDetailsDto setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public UserPermissionTargetCompanyDetailsDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }
}
