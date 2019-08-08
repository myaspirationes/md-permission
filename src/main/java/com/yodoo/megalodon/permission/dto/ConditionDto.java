package com.yodoo.megalodon.permission.dto;

/**
 * @Description ：传输条件
 * @Author ：jinjun_luo
 * @Date ： 2019/8/8 0008
 */
public class ConditionDto  {

    /**
     * 查询条件id
     */
    private Integer searchConditionId;
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

    public ConditionDto setSearchConditionId(Integer searchConditionId) {
        this.searchConditionId = searchConditionId;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public ConditionDto setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getMatchValue() {
        return matchValue;
    }

    public ConditionDto setMatchValue(String matchValue) {
        this.matchValue = matchValue;
        return this;
    }
}
