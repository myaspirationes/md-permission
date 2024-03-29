package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

/**
 * @Description ：查询条件
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class SearchCondition extends BaseEntity {

    /**
     * 条件名称
     **/
    private String conditionName;
    /**
     * 条件code
     **/
    private String conditionCode;

    /**
     * 条件值
     */
    private String conditionValue;

    public SearchCondition() {
    }

    public SearchCondition(String conditionName, String conditionCode, String conditionValue) {
        this.conditionName = conditionName;
        this.conditionCode = conditionCode;
        this.conditionValue = conditionValue;
    }

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

    public String getConditionValue() {
        return conditionValue;
    }

    public SearchCondition setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
        return this;
    }
}