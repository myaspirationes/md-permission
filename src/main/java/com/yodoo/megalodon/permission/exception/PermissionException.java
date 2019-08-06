package com.yodoo.megalodon.permission.exception;

public class PermissionException extends RuntimeException {
    private String messageBundleKey;

    private String[] messageParams;

    public PermissionException() {
        super();
    }

    public PermissionException(String messageBundleKey, String... messageParams) {
        super(messageBundleKey);
        this.messageParams = messageParams;
        this.messageBundleKey = messageBundleKey;
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessageBundleKey() {
        return messageBundleKey;
    }

    public void setMessageBundleKey(String messageBundleKey) {
        this.messageBundleKey = messageBundleKey;
    }

    public String[] getMessageParams() {
        return messageParams;
    }

    public void setMessageParams(String[] messageParams) {
        this.messageParams = messageParams;
    }
}
