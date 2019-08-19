package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;
import com.yodoo.megalodon.permission.dto.SearchConditionDto;

import java.util.List;

/**
 * @Description ：用户组表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserGroup extends BaseEntity {

    /**
     * 用户组code
     **/
    private String groupCode;

    /**
     * 用户组名称
     **/
    private String groupName;

    /**
     * 用户组所拥有的条件
     */
    private List<SearchConditionDto> searchConditionDtoList;

    public UserGroup() {
    }

    public UserGroup(String groupCode, String groupName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public List<SearchConditionDto> getSearchConditionDtoList() {
        return searchConditionDtoList;
    }

    public UserGroup setSearchConditionDtoList(List<SearchConditionDto> searchConditionDtoList) {
        this.searchConditionDtoList = searchConditionDtoList;
        return this;
    }
}