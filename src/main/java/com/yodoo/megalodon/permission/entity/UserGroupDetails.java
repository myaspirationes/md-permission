package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 用户组关系明细表
 */
public class UserGroupDetails extends BaseEntity {

    /**
     * 用户组id
     **/
    private Integer userGroupId;

    /**
     * 用户id
     **/
    private Integer userId;

    public UserGroupDetails() {
    }

    public UserGroupDetails(Integer userGroupId, Integer userId) {
        this.userGroupId = userGroupId;
        this.userId = userId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}