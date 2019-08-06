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

}
