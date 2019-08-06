package com.yodoo.megalodon.permission.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtils {

    public static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    public static boolean isContainEmpty(String... args) {
        if (args == null) {
            return false;
        }

        for (String arg : args) {
            if (arg == null || "".equals(arg)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }


    public static String trim(String str) {
        return str == null ? null : str.trim();
    }


    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }


    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }


    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }


    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }


    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public interface StringFormatter<T> {
        /**
         * 格式化
         *
         * @param obj
         * @return
         */
        String format(T obj);
    }

    public static <T> String join(Collection<T> collection, String separator) {
        return join(collection, separator, new StringFormatter<T>() {
            @Override
            public String format(T obj) {
                return obj.toString();
            }
        });
    }

    public static <T> String join(Collection<T> collection, String separator,
                                  StringFormatter<T> formatter) {
        Iterator<T> iterator = collection.iterator();
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        T first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? "" : formatter.format(first);
        }

        // two or more elements
        // Java default is 16, probably too small
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(formatter.format(first));
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            T obj = iterator.next();
            if (obj != null) {
                buf.append(formatter.format(obj));
            }
        }

        return buf.toString();
    }
}
