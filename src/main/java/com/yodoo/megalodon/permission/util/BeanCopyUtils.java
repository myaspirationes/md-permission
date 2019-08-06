package com.yodoo.megalodon.permission.util;

import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date 2019/6/11 9:36
 * @Created by houzhen
 */
public class BeanCopyUtils {

    public static <T> List<T> copyList(List<?> list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JsonUtils.json2list(JsonUtils.obj2json(list), clazz);
    }

    public static Map<String, Object> copyMap(Map map) {
        return JsonUtils.json2map(JsonUtils.obj2json(map));
    }

    public static <T> T convertMapToBean(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            //写数据
            for (Field field : fields) {
                Object value = map.get(field.getName());
                if (value == null) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                // 获得写方法
                Method method = pd.getWriteMethod();
                method.invoke(obj, getClassTypeValue(field.getType(), value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object getClassTypeValue(Class<?> typeClass, Object value) {
        Object typeValue = value;
        if (value == null) {
            typeValue = null;
        } else if (typeClass == String.class) {
            typeValue = String.valueOf(value);
        } else if (typeClass == int.class || typeClass == Integer.class) {
            typeValue = Integer.parseInt(String.valueOf(value));
        } else if (typeClass == short.class || typeClass == Short.class) {
            typeValue = Short.valueOf(String.valueOf(value));
        } else if (typeClass == byte.class || typeClass == Byte.class) {
            typeValue = Byte.valueOf(String.valueOf(value));
        } else if (typeClass == double.class || typeClass == Double.class) {
            typeValue = Double.valueOf(String.valueOf(value));
        } else if (typeClass == long.class || typeClass == Long.class) {
            typeValue = Long.valueOf(String.valueOf(value));
        } else if (typeClass == boolean.class || typeClass == Boolean.class) {
            typeValue = "true".equals(String.valueOf(value));
        } else if (typeClass == BigDecimal.class) {
            typeValue = new BigDecimal(String.valueOf(value));
        } else if (typeClass == Date.class) {
            Date d = (Date) value;
        }
        return typeValue;
    }

}
