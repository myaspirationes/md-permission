package com.yodoo.megalodon.permission.dto;


import com.yodoo.megalodon.permission.common.BaseDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Set;

/**
 * @Description ：用户管理
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public class UserDto extends BaseDto {

    /**
     * 父id
     **/
    @ApiModelProperty(value = "父id", required = false, example = "1", position = 1)
    private Integer parentId;

    /**
     * 账号
     **/
    @ApiModelProperty(value = "账号", required = true, example = "account_name", position = 2)
    private String account;

    /**
     * 用户名称
     **/
    @ApiModelProperty(value = "用户名称", required = true, example = "user_name", position = 3)
    private String name;

    /**
     * 密码
     **/
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * 邮箱
     **/
    @ApiModelProperty(value = "邮箱", required = true, example = "user_email", position = 4)
    private String email;

    /**
     * 区域
     **/
    @ApiModelProperty(value = "区域", required = true, example = "shanghai", position = 5)
    private String region;

    /**
     * 岗位
     **/
    @ApiModelProperty(value = "岗位", required = true, example = "user_post", position = 6)
    private String post;

    /**
     * 性别：0 没指定性别，1 男， 2 女
     **/
    @ApiModelProperty(value = "性别：0 没指定性别，1 男， 2 女", required = true, example = "0", position = 7)
    private Integer sex;

    /**
     * 出生日期
     **/
    @ApiModelProperty(value = "出生日期", required = true, example = "2019-10-01", position = 8)
    private Date birthday;

    /**
     * 电话
     **/
    @ApiModelProperty(value = "电话", required = true, example = "13804389438", position = 9)
    private String phone;

    /**
     * 状态，0：启用 1：停用
     **/
    @ApiModelProperty(hidden = true)
    private Integer status;

    /**
     * 用户组ids
     */
    @ApiModelProperty(hidden = true)
    private Set<Integer> userGroupIds;

    public Integer getParentId() {
        return parentId;
    }

    public UserDto setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public UserDto setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public UserDto setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getPost() {
        return post;
    }

    public UserDto setPost(String post) {
        this.post = post;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public UserDto setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public UserDto setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public UserDto setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Set<Integer> getUserGroupIds() {
        return userGroupIds;
    }

    public UserDto setUserGroupIds(Set<Integer> userGroupIds) {
        this.userGroupIds = userGroupIds;
        return this;
    }
}
