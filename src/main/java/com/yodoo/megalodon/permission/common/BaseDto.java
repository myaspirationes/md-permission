package com.yodoo.megalodon.permission.common;

import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(hidden = true)
    private int pageNum;

    /**
     * 页面大小
     */
    @ApiModelProperty(hidden = true)
    private int pageSize;

    /**
     * 创建人
     **/
    @ApiModelProperty(hidden = true)
    private Integer createdBy;

    /**
     * 创建日期
     **/
    @ApiModelProperty(hidden = true)
    private Instant createdTime;

    /**
     * 更新人
     **/
    @ApiModelProperty(hidden = true)
    private Integer lastModifiedBy;

    /**
     * 最后一次修改时间
     **/
    @ApiModelProperty(hidden = true)
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
