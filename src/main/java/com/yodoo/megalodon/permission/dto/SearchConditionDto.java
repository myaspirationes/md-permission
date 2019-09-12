package com.yodoo.megalodon.permission.dto;

import com.yodoo.megalodon.permission.common.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：条件查询表
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
public class SearchConditionDto extends BaseDto {

    /**
     * 条件名称
     **/
    @ApiModelProperty(value = "条件名称", required = true, example = "name", position = 1)
    private String conditionName;

    /**
     * 条件code
     **/
    @ApiModelProperty(value = "条件code", required = true, example = "code", position = 2)
    private String conditionCode;

    /**
     * 条件值
     */
    @ApiModelProperty(value = "条件值", required = true, example = "value", position = 3)
    private String conditionValue;

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

    public String getConditionValue() {
        return conditionValue;
    }

    public SearchConditionDto setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
        return this;
    }
}
