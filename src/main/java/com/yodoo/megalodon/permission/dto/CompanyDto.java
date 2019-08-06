package com.yodoo.megalodon.permission.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.megalodon.permission.common.BaseDto;

import java.util.Date;

/**
 * @Author houzhen
 * @Date 13:10 2019/7/29
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto extends BaseDto {

    /**
     * 集团id
     **/
    private Integer groupId;

    /**
     * 公司名称
     **/
    private String companyName;

    /**
     * 公司Code
     **/
    private String companyCode;

    /**
     * 更新周期
     **/
    private String updateCycle;

    /**
     * 下次更新日期
     **/
    private Date nextUpdateDate;

    /**
     * 到期日
     **/
    private Date expireDate;

    /**
     * DB数据库组id
     **/
    private Integer dbGroupId;

    /**
     * redis组id
     **/
    private Integer redisGroupId;

    /**
     * swift租户id
     **/
    private Integer swiftProjectId;

    /**
     * 消息队列vhost
     **/
    private Integer mqVhostId;

    /**
     * neo4j实例id
     **/
    private Integer neo4jInstanceId;

    /**
     * 状态，0：创建中，1：创建完成,启用中， 2：停用
     **/
    private Integer status;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getDbGroupId() {
        return dbGroupId;
    }

    public void setDbGroupId(Integer dbGroupId) {
        this.dbGroupId = dbGroupId;
    }

    public Integer getRedisGroupId() {
        return redisGroupId;
    }

    public void setRedisGroupId(Integer redisGroupId) {
        this.redisGroupId = redisGroupId;
    }

    public Integer getSwiftProjectId() {
        return swiftProjectId;
    }

    public void setSwiftProjectId(Integer swiftProjectId) {
        this.swiftProjectId = swiftProjectId;
    }

    public Integer getMqVhostId() {
        return mqVhostId;
    }

    public void setMqVhostId(Integer mqVhostId) {
        this.mqVhostId = mqVhostId;
    }

    public Integer getNeo4jInstanceId() {
        return neo4jInstanceId;
    }

    public void setNeo4jInstanceId(Integer neo4jInstanceId) {
        this.neo4jInstanceId = neo4jInstanceId;
    }

    public Integer getStatus() {
        return status;
    }

    public CompanyDto setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
