package com.gy.crawler.utils;

import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author hongjun luo
 * @ClassName JSONUtil
 * @date
 */
public class JSONUtil {


    public static String object2JacksonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("瀵硅薄杞寲涓簀son鏁版嵁鍑洪敊锛屽彲鑳芥槸瀵硅薄绫诲瀷涓嶅尮閰�");
        }
    }


    public static Object jackson2Object(String jsonString, Class<?> destClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, destClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("json鏁版嵁杞寲涓哄璞℃椂鍑洪敊锛屽彲鑳芥槸瀵硅薄绫诲瀷涓嶅尮閰�");
        }
    }


    public static List jackson2List(String jsonString, Class<?> collectionClass, Class<?> elementClasses) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        List list = null;
        try {
            list = objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("json鏁版嵁杞寲涓哄璞℃椂鍑洪敊锛屽彲鑳芥槸瀵硅薄绫诲瀷涓嶅尮閰�");
        }
        return list;
    }

}
