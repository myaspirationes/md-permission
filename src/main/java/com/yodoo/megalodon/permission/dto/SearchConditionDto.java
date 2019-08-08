package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;

/**
 * @Description ：条件查询表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
public class SearchConditionDto extends BaseDto {

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

    public SearchConditionDto setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
        return this;
    }

    public String getConditionName() {
        return conditionName;
    }

    public SearchConditionDto setConditionName(String conditionName) {
        this.conditionName = conditionName;
        return this;
    }
}
