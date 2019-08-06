package com.yodoo.megalodon.permission.util;


import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.yodoo.megalodon.permission.exception.BundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;


public class RequestPrecondition {

    public static void checkArgumentsNotEmpty(String... args) {
        checkArguments(!StringUtils.isContainEmpty(args));
    }

    public static void checkModel(boolean valid) {
        checkArguments(valid);
    }

    public static void checkArguments(boolean expression) {
        if (!expression) {
            throw new PermissionException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
    }
}
