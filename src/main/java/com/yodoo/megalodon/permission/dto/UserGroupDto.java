package com.yodoo.megalodon.permission.dto;


import com.yodoo.megalodon.permission.common.BaseDto;

import javax.validation.constraints.Null;
import java.util.List;
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
     * 查询条件 ids
     */
    private List<Integer> searchConditionIds;

    /**
     * 用户详情
     */
    private List<UserDto> userDtoList;

    /**
     * 用户组筛选条件详情
     */
     private List<SearchConditionDto> searchConditionDtoList;

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

    public List<Integer> getSearchConditionIds() {
        return searchConditionIds;
    }

    public UserGroupDto setSearchConditionIds(List<Integer> searchConditionIds) {
        this.searchConditionIds = searchConditionIds;
        return this;
    }

    public List<UserDto> getUserDtoList() {
        return userDtoList;
    }

    public UserGroupDto setUserDtoList(List<UserDto> userDtoList) {
        this.userDtoList = userDtoList;
        return this;
    }

    public List<SearchConditionDto> getSearchConditionDtoList() {
        return searchConditionDtoList;
    }

    public UserGroupDto setSearchConditionDtoList(List<SearchConditionDto> searchConditionDtoList) {
        this.searchConditionDtoList = searchConditionDtoList;
        return this;
    }
}
