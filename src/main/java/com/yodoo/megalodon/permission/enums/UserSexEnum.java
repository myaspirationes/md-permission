package com.yodoo.megalodon.permission.enums;

/**
 * @Description ：性别：0 没指定性别，1 男， 2 女
 * @Author ：jinjun_luo
 * @Date ： 2019/8/6 0006
 */
public enum UserSexEnum {

    /**
     * 无性别
     */
    SEXLESS(0, "无性别"),
    /**
     * 男
     **/
    MAN(1, "男"),

    /**
     * 女
     **/
    GIRL(2, "女");

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    UserSexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
