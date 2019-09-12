package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;
import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(value = "用户id", required = true, example = "1", position = 1)
    private Integer userId;

    /**
     * 用户权限id
     */
    @ApiModelProperty(value = "用户权限id", required = true, example = "1", position = 2)
    private Integer permissionId;

    /**
     * 目标集团、目标公司或目标用户ids
     */
    @ApiModelProperty(value = "目标集团、目标公司或目标用户ids", required = true, example = "[1,2]", position = 3)
    private Set<Integer> targetIds;

    public Integer getUserId() {
        return userId;
    }

    public UserPermissionTargetDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public UserPermissionTargetDto setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
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
