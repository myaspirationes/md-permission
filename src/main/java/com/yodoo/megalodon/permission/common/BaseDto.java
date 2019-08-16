package com.yodoo.megalodon.permission.common;

import java.time.Instant;
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
    private Integer createdBy;

    /**
     * 创建日期
     **/
    private Instant createdTime;

    /**
     * 更新人
     **/
    private Integer lastModifiedBy;

    /**
     * 最后一次修改时间
     **/
    private Instant lastModifiedTime;

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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public BaseDto setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public BaseDto setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public BaseDto setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public BaseDto setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }
}
