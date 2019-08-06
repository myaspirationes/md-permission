package com.yodoo.megalodon.permission.entity;

import com.yodoo.megalodon.permission.common.BaseEntity;

import java.util.Date;

/**
 * 用户表
 */
public class User extends BaseEntity {

    /**
     * 父id
     **/
    private Integer parentId;

    /**
     * 账号
     **/
    private String account;

    /**
     * 用户名称
     **/
    private String name;

    /**
     * 密码
     **/
    private String password;

    /**
     * 邮箱
     **/
    private String email;

    /**
     * 区域
     **/
    private String region;

    /**
     * 岗位
     **/
    private String post;

    /**
     * 性别：0 没指定性别，1 男， 2 女
     **/
    private Integer sex;

    /**
     * 出生日期
     **/
    private Date birthday;

    /**
     * 电话
     **/
    private String phone;

    /**
     * 状态，0：启用 1：停用
     **/
    private Integer status;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}