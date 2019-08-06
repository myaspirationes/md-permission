package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 用户组条件表
 */
public class UserGroupCondition extends BaseEntity {

    /**
     * 用户组条件id
     **/
    private Integer searchConditionId;

    /**
     * 用户组id
     **/
    private Integer userGroupId;

    /**
     * 运算符号
     **/
    private String operator;

    /**
     * 匹配值
     **/
    private String matchValue;

    public Integer getSearchConditionId() {
        return searchConditionId;
    }

    public void setSearchConditionId(Integer searchConditionId) {
        this.searchConditionId = searchConditionId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(String matchValue) {
        this.matchValue = matchValue == null ? null : matchValue.trim();
    }

}