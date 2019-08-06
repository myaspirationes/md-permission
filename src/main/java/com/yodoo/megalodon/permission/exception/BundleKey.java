package com.yodoo.megalodon.permission.exception;

public interface BundleKey {

    /**
     * permission系统前缀
     **/
    String PERMISSION_PREFIX = "MEGALODON.PERMISSION.";

    /**
     * 成功
     **/
    String SUCCESS = PERMISSION_PREFIX + "SUCCESS";
    String SUCCESS_MSG = "请求成功";

    /**
     * 失败
     **/
    String FAIL = PERMISSION_PREFIX + "FAIL";
    String FAIL_MSG = "请求失败";

    /**
     * 未定义、未捕获、未处理的异常
     **/
    String UNDEFINED = PERMISSION_PREFIX + "UNDEFINED";
    String UNDEFINED_MSG = "服务异常,请联系管理员";

    /**
     * 参数异常
     **/
    String PARAMS_ERROR = PERMISSION_PREFIX + "PARAMS.ERROR";
    String PARAMS_ERROR_MSG = "参数异常";

    /**
     * 权限已存在
     */
    String PERMISSION_EXIST = PERMISSION_PREFIX + "PERMISSION.EXIST";
    String PERMISSION_EXIST_MSG = "权限已存在";

    /**
     * 权限已存在
     */
    String PERMISSION_NOT_EXIST = PERMISSION_PREFIX + "PERMISSION.NOT.EXIST";
    String PERMISSION_NOT_EXIST_MSG = "权限不存在";

    /**
     * 权限组不存在
     **/
    String PERMISSION_GROUP_NOT_EXIST = PERMISSION_PREFIX + "PERMISSION.GROUP.NOT.EXIST";
    String PERMISSION_GROUP_NOT_EXIST_MSG = "权限组不存在";

    /**
     * 权限组已存在 PermissionGroup
     */
    String PERMISSION_GROUP_ALREADY_EXIST = PERMISSION_PREFIX + "PERMISSION.GROUP.ALREADY.EXIST";
    String PERMISSION_GROUP_ALREADY_EXIST_MSG = "权限组已存在";

    /**
     * 数据有在使用
     */
    String THE_DATA_IS_STILL_IN_USE = PERMISSION_PREFIX + "THE.DATA.IS.STILL.IN.USE";
    String THE_DATA_IS_STILL_IN_USE_MEG = "数据有在使用";

    /**
     *  userGroup 用户组不存在
     **/
    String USER_GROUP_NOT_EXIST = PERMISSION_PREFIX + "USER.GROUP.NOT.EXIST";
    String USER_GROUP_NOT_EXIST_MSG = "用户组不存在";

    /**
     *  userGroup 用户组已存在
     **/
    String USER_GROUP_ALREADY_EXIST = PERMISSION_PREFIX + "USER.GROUP.ALREADY.EXIST";
    String USER_GROUP_ALREADY_EXIST_MSG = "用户组已存在";

    /**
     *  User 用户已存在
     **/
    String USER_ALREADY_EXIST = PERMISSION_PREFIX + "USER.ALREADY.EXIST";
    String USER_ALREADY_EXIST_MSG = "用户已存在";

    /**
     *  User 用户不存在
     **/
    String USER_NOT_EXIST = PERMISSION_PREFIX + "USER.NOT.EXIST";
    String USER_NOT_EXIST_MSG = "用户不存在";

    /**
     *  邮箱格式错误 Email format error
     **/
    String EMAIL_FORMAT_ERROR = PERMISSION_PREFIX + "EMAIL.FORMAT.ERROR";
    String EMAIL_FORMAT_ERROR_MSG = "邮箱格式错误";

    /**
     *  电话格式错误 phone format error
     **/
    String PHONE_FORMAT_ERROR = PERMISSION_PREFIX + "PHONE.FORMAT.ERROR";
    String PHONE_FORMAT_ERROR_MSG = "电话格式错误";

}
