package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

import java.util.Set;

/**
 * @Description ：管理目标集团，目标公司，目标用户更新传参数
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
public class UserPermissionTargetDto extends BaseDto {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户权限id
     */
    private Integer userPermissionId;

    /**
     * 目标集团
     */
    private Set<Integer> targetIds;

    public Integer getUserId() {
        return userId;
    }

    public UserPermissionTargetDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public UserPermissionTargetDto setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
        return this;
    }

    public Set<Integer> getTargetIds() {
        return targetIds;
    }

    public UserPermissionTargetDto setTargetIds(Set<Integer> targetIds) {
        this.targetIds = targetIds;
        return this;
    }
}
