package com.yodoo.megalodon.permission.util;


import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.yodoo.megalodon.permission.exception.PermissionBundleKey;
import com.yodoo.megalodon.permission.exception.PermissionException;

/**
 * @Date 2019/7/26 13:17
 * @author by houzhen
 */
public class RequestPrecondition {

    public static void checkArgumentsNotEmpty(String... args) {
        checkArguments(!StringUtils.isContainEmpty(args));
    }

    public static void checkModel(boolean valid) {
        checkArguments(valid);
    }

    public static void checkArguments(boolean expression) {
        if (!expression) {
            throw new PermissionException(PermissionBundleKey.PARAMS_ERROR, PermissionBundleKey.PARAMS_ERROR_MSG);
        }
    }
}
