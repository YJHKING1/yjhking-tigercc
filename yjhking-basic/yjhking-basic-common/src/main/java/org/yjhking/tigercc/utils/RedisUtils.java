package org.yjhking.tigercc.utils;

import org.yjhking.tigercc.constants.RedisConstants;

/**
 * redis工具类
 *
 * @author YJH
 */
public class RedisUtils {
    public static String getSmsCodeTime(String codeString) {
        return codeString.split(RedisConstants.REDIS_VERIFY)[RedisConstants.REDIS_VERIFY_SECOND];
    }
    
    public static String getSmsCode(String codeString) {
        return codeString.split(RedisConstants.REDIS_VERIFY)[RedisConstants.REDIS_VERIFY_FIRST];
    }
}
