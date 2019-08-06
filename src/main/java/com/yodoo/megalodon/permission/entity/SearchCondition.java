package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * 查询条件
 */
public class SearchCondition extends BaseEntity {

    /**
     * 条件code
     **/
    private String conditionCode;

    /**
     * 条件名称
     **/
    private String conditionName;

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode == null ? null : conditionCode.trim();
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName == null ? null : conditionName.trim();
    }

}