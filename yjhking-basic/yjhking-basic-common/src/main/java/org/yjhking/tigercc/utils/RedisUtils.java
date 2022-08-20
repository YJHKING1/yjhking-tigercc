package org.yjhking.tigercc.utils;

import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;

/**
 * redis工具类
 *
 * @author YJH
 */
public class RedisUtils {
    public static String getSmsCodeTime(String codeString) {
        return codeString.split(RedisConstants.REDIS_VERIFY)[NumberConstants.ONE];
    }
    
    public static String getSmsCode(String codeString) {
        return codeString.split(RedisConstants.REDIS_VERIFY)[NumberConstants.ZERO];
    }
}
