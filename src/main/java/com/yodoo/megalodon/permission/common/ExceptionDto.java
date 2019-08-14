package com.yodoo.megalodon.permission.common;

/**
 * @Author houzhen
 * @Date 2019/5/14 16:40
 **/
public class ExceptionDto<T> {

    /**
     * 请求状态 -1：未登录 0：成功 1：失败 2：无权限
     */
    private String status;

    /**
     * 状态码
     */
    private String messageBundleKey;

    /**
     * 返回操作备注信息
     */
    private String message;

    /***封装数据信息*/
    private T data;


    public ExceptionDto() {

    }

    public ExceptionDto(String status, String messageBundleKey, String message) {
        this.status = status;
        this.messageBundleKey = messageBundleKey;
        this.message = message;
    }

    public ExceptionDto(String status, String messageBundleKey, String message, T data) {
        this.status = status;
        this.messageBundleKey = messageBundleKey;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageBundleKey() {
        return messageBundleKey;
    }

    public void setMessageBundleKey(String messageBundleKey) {
        this.messageBundleKey = messageBundleKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
