package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

import java.util.Date;

/**
 * 权限
 * @Author houzhen
 * @Date 15:22 2019/8/5
**/
public class GroupsDto extends BaseDto {

    /**
     * 集团名称
     **/
    private String groupName;

    /**
     * 集团Code
     **/
    private String groupCode;

    /**
     * 到期日
     **/
    private Date expireDate;

    /**
     * 更新周期
     **/
    private String updateCycle;

    /**
     * 下次更新日期
     **/
    private Date nextUpdateDate;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getUpdateCycle() {
        return updateCycle;
    }

    public void setUpdateCycle(String updateCycle) {
        this.updateCycle = updateCycle;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }
}
