package com.yodoo.megalodon.permission.common;

import java.util.Date;

/**
 * @Date 2019/7/26 13:17
 * @author by houzhen
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

        /**
     * 创建人
     **/
    private String createUser;

    /**
     * 创建日期
     **/
    private Date createTime;

    /**
     * 更新人
     **/
    private String updateUser;

    /**
     * 最后一次修改时间
     **/
    private Date updateTime;

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

    public String getCreateUser() {
        return createUser;
    }

    public BaseDto setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public BaseDto setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public BaseDto setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public BaseDto setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
