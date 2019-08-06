package com.yodoo.megalodon.permission.enums;

/**
 * @Description ：用户状态
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public enum  UserStatusEnum {

    /**
     * 已被使用 状态，0：启用 1：停用
     **/
    USE(0, "启用"),

    /**
     * 已被使用 状态，0：启用 1：停用
     **/
    STOP(1, "停用");

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    UserStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
