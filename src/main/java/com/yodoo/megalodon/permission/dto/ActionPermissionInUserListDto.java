package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

import java.util.List;

/**
 * @Description ：在用户列表点击权限，获取目标集团，目标公司，目标用户
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
public class ActionPermissionInUserListDto extends BaseDto {

    /**
     * 目标集团列表
     */
    private List<UserPermissionTargetGroupDetailsDto> userPermissionTargetGroupDetailsDtoList;

    /**
     * 目标公司
     */
    private List<UserPermissionTargetCompanyDetailsDto> userPermissionTargetCompanyDetailsDtoList;

    /**
     * 目标用户
     */
    private List<UserPermissionTargetUserDetailsDto> userPermissionTargetUserDetailsDtoList;

    public List<UserPermissionTargetGroupDetailsDto> getUserPermissionTargetGroupDetailsDtoList() {
        return userPermissionTargetGroupDetailsDtoList;
    }

    public ActionPermissionInUserListDto setUserPermissionTargetGroupDetailsDtoList(List<UserPermissionTargetGroupDetailsDto> userPermissionTargetGroupDetailsDtoList) {
        this.userPermissionTargetGroupDetailsDtoList = userPermissionTargetGroupDetailsDtoList;
        return this;
    }

    public List<UserPermissionTargetCompanyDetailsDto> getUserPermissionTargetCompanyDetailsDtoList() {
        return userPermissionTargetCompanyDetailsDtoList;
    }

    public ActionPermissionInUserListDto setUserPermissionTargetCompanyDetailsDtoList(List<UserPermissionTargetCompanyDetailsDto> userPermissionTargetCompanyDetailsDtoList) {
        this.userPermissionTargetCompanyDetailsDtoList = userPermissionTargetCompanyDetailsDtoList;
        return this;
    }

    public List<UserPermissionTargetUserDetailsDto> getUserPermissionTargetUserDetailsDtoList() {
        return userPermissionTargetUserDetailsDtoList;
    }

    public ActionPermissionInUserListDto setUserPermissionTargetUserDetailsDtoList(List<UserPermissionTargetUserDetailsDto> userPermissionTargetUserDetailsDtoList) {
        this.userPermissionTargetUserDetailsDtoList = userPermissionTargetUserDetailsDtoList;
        return this;
    }
}
