package com.yodoo.megalodon.permission.common;

/**
 * @Date 2019/7/26 13:17
 * @Created by houzhen
 */
public class BaseDto {

    private Integer id;

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 页面大小
     */
    private int pageSize;

    public BaseDto() {
    }

    public BaseDto(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPageNum() {
        return pageNum;
    }

    public BaseDto setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public BaseDto setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
