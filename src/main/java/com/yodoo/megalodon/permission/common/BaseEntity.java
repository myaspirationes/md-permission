package com.yodoo.megalodon.permission.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

/**
 * @Date 2019/6/10 20:03
 * @author  by houzhen
 */
public class BaseEntity {
    /**
     * id 主键
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public BaseEntity setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public BaseEntity setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public BaseEntity setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public BaseEntity setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setCreatedBy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            Integer userId = null;
            if (principal instanceof User){
                User user = (User) authentication.getPrincipal();
                userId = Integer.valueOf(user.getUsername());
            }else {
                userId = (Integer) authentication.getPrincipal();
            }
            this.createdBy = userId;
        }
    }

    public void setLastModifiedBy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Integer userId = null;
            Object principal = authentication.getPrincipal();
            if (principal instanceof User){
                User user = (User) authentication.getPrincipal();
                userId = Integer.valueOf(user.getUsername());
            }else {
                userId = (Integer) authentication.getPrincipal();
            }
            this.lastModifiedBy = userId;
            this.lastModifiedTime = Instant.now();
        }
    }

}
