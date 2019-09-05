package com.yodoo.megalodon.permission.contract;

/**
 * @Author houzhen
 * @Date 14:39 2019/7/29
 **/
public interface PermissionConstants {

    /**
     * session中存放登录信息
     */
    String AUTH_USER = "AUTH_USER";

    /**
     * 邮箱校验正则
     */
    String EMAIL_SERVER_MAILBOX_REGULAR_EXPRESSION = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    /**
     * 添加用户默认密码
     */
    String DEFAULT_PASSWORD = "yodoo123";

    /**
     * 电话号码长度
     */
    Integer PHONE_LENGTH = 11;

    /**
     * 字符串中全是0到9数字
     */
    String SEARCH_CONDITION_VALUE = "[0-9]{1,}";

    String STARTS_WITH = "/";
}
