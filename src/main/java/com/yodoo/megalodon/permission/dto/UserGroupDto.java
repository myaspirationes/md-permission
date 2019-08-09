package com.yodoo.megalodon.permission.dto;


import com.yodoo.megalodon.permission.common.BaseDto;

import java.util.Set;

/**
 * @Description ：用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserGroupDto extends BaseDto {

    /**
     * 用户组code
     **/
    private String groupCode;

    /**
     * 用户组名称
     **/
    private String groupName;

    /**
     * 当前组下用户总数
     */
    private Integer userTotal;

    /**
     * 权限组 ids
     */
    private Set<Integer> permissionGroupIds;

    /**
     * 查询条件 List
     */
    private Set<ConditionDto> conditionDtoList;

    public String getGroupCode() {
        return groupCode;
    }

    public UserGroupDto setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public UserGroupDto setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public Integer getUserTotal() {
        return userTotal;
    }

    public UserGroupDto setUserTotal(Integer userTotal) {
        this.userTotal = userTotal;
        return this;
    }

    public Set<Integer> getPermissionGroupIds() {
        return permissionGroupIds;
    }

    public UserGroupDto setPermissionGroupIds(Set<Integer> permissionGroupIds) {
        this.permissionGroupIds = permissionGroupIds;
        return this;
    }

    public Set<ConditionDto> getConditionDtoList() {
        return conditionDtoList;
    }

    public UserGroupDto setConditionDtoList(Set<ConditionDto> conditionDtoList) {
        this.conditionDtoList = conditionDtoList;
        return this;
    }
}
