package com.yodoo.megalodon.permission.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json string to get key
     */
    public static String getJsonValue(String jsonStr, String key) {
        Map<String, Object> jsonMap = json2map(jsonStr);
        if (jsonMap != null) {
            return obj2json(jsonMap.get(key));
        }
        return null;
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, String key, Class<T> clazz) {
        String tempJsonStr = getJsonValue(jsonStr, key);
        if (!StringUtils.isBlank(tempJsonStr)) {
            return json2pojo(tempJsonStr, clazz);
        }
        return null;
    }

    /**
     * json string convert to map
     */
    public static <T> Map<String, Object> json2map(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
        Map<String, T> result = new HashMap<String, T>();
        try {
            Map<String, Map<String, Object>> map = OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, T>>() {
            });
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        try {
            result = OBJECT_MAPPER.readValue(jsonArrayStr, OBJECT_MAPPER.getTypeFactory().constructParametricType(ArrayList.class, clazz));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonStr, String key, Class<T> clazz) {
        String tempJsonStr = getJsonValue(jsonStr, key);
        if (!StringUtils.isBlank(tempJsonStr)) {
            return json2list(tempJsonStr, clazz);
        }
        return null;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }
}