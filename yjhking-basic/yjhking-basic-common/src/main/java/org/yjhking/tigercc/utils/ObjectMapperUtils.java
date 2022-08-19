package org.yjhking.tigercc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author YJH
 */
public class ObjectMapperUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }
}
