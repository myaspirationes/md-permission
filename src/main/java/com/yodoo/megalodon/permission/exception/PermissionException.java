package com.yodoo.megalodon.permission.exception;

/**
 * @Date 2019/7/11 10:56
 * @author  by houzhen
 */
public class PermissionException extends RuntimeException {
    private String permissionMessageBundleKey;

    private String[] permissionMessageParams;

    public PermissionException() {
        super();
    }

    public PermissionException(String messageBundleKey, String... messageParams) {
        super(messageBundleKey);
        this.permissionMessageParams = messageParams;
        this.permissionMessageBundleKey = messageBundleKey;
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getPermissionMessageBundleKey() {
        return permissionMessageBundleKey;
    }

    public void setPermissionMessageBundleKey(String permissionMessageBundleKey) {
        this.permissionMessageBundleKey = permissionMessageBundleKey;
    }

    public String[] getPermissionMessageParams() {
        return permissionMessageParams;
    }

    public void setPermissionMessageParams(String[] permissionMessageParams) {
        this.permissionMessageParams = permissionMessageParams;
    }
}
