package org.yjhking.tigercc.utils;

import org.yjhking.tigercc.constants.RegularConstants;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author YJH
 */
public class VerificationUtils {
    /**
     * 字符串非空校验
     *
     * @param str 字符串
     * @return true:非空 false:空
     */
    public static boolean stringVerification(String str) {
        return str != null && str.trim().length() > 0;
    }
    
    /**
     * 对象非空校验
     *
     * @param obj 对象
     * @return true:非空 false:空
     */
    public static boolean objectVerification(Object obj) {
        return obj != null;
    }
    
    /**
     * list集合非空校验
     *
     * @param list list集合
     * @return true:非空 false:空
     */
    public static <T> boolean listVerification(List<T> list) {
        return list != null && list.size() > 0;
    }
    
    /**
     * 断言手机号格式正确
     *
     * @param phone 手机号
     */
    public static void isPhone(String phone) {
        if (!Pattern.compile(RegularConstants.PHONE_REGULAR).matcher(phone).matches())
            throwGlobalCustomException(GlobalErrorCode.USER_PHONE_FORMAT_ERROR);
    }
    
    /**
     * 断言字符串不为空
     *
     * @param text 字符串
     */
    public static void isNotEmpty(String text) {
        if (text == null || text.trim().length() == 0)
            throwGlobalCustomException(GlobalErrorCode.SERVICE_PARAM_IS_NULL);
    }
    
    /**
     * 断言对象为空
     *
     * @param obj 对象
     */
    public static void isNull(Object obj) {
        if (obj != null) throwGlobalCustomException(GlobalErrorCode.SERVICE_OBJECT_IS_NOT_NULL);
    }
    
    /**
     * 断言对象不为空
     *
     * @param obj 对象
     */
    public static void isNotNull(Object obj) {
        if (obj == null) throwGlobalCustomException(GlobalErrorCode.SERVICE_OBJECT_IS_NULL);
    }
    
    /**
     * 断言为假
     *
     * @param isFalse 判断值
     */
    public static void isFalse(boolean isFalse) {
        if (isFalse) throwGlobalCustomException(GlobalErrorCode.SERVICE_PARAM_IS_TRUE);
    }
    
    /**
     * 断言为真
     *
     * @param isTrue 判断值
     */
    public static void isTrue(boolean isTrue) {
        if (!isTrue) throwGlobalCustomException(GlobalErrorCode.SERVICE_PARAM_IS_FALSE);
    }
    
    /**
     * 断言两个字符串一致
     *
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     */
    public static void isEquals(String s1, String s2) {
        isNotEmpty(s1);
        isNotEmpty(s2);
        if (!s1.equals(s2)) throwGlobalCustomException(GlobalErrorCode.SERVICE_PARAM_IS_NOT_EQUALS);
    }
    
    /**
     * 断言两个字符串一致，去空格
     *
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     */
    public static void isEqualsTrim(String s1, String s2) {
        isNotEmpty(s1);
        isNotEmpty(s2);
        if (!s1.trim().equals(s2.trim())) throwGlobalCustomException(GlobalErrorCode.SERVICE_PARAM_IS_NOT_EQUALS);
    }
    
    /**
     * 断言两个字符串一致，去空格且忽略大小写
     *
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     */
    public static void isEqualsIgnoreCase(String s1, String s2) {
        isNotEmpty(s1);
        isNotEmpty(s2);
        if (!s1.trim().equalsIgnoreCase(s2.trim()))
            throwGlobalCustomException(GlobalErrorCode.SERVICE_PARAM_IS_NOT_EQUALS);
    }
    
    /**
     * 断言集合为空
     *
     * @param list 集合
     */
    public static <T> void listIsNull(List<T> list) {
        if (list == null) return;
        else if (list.size() != 0) throwGlobalCustomException(GlobalErrorCode.SERVICE_LIST_IS_NOT_NULL);
    }
    
    /**
     * 断言集合不为空
     *
     * @param list 集合
     */
    public static <T> void listIsNotNull(List<T> list) {
        if (list == null || list.size() == 0) throwGlobalCustomException(GlobalErrorCode.SERVICE_LIST_IS_NULL);
    }
    
    /**
     * 抛出自定义异常
     *
     * @param errorCode 自定义异常状态码
     */
    private static void throwGlobalCustomException(GlobalErrorCode errorCode) {
        throw new GlobalCustomException(errorCode);
    }
    
    /**
     * 断言手机号格式正确
     *
     * @param phone 手机号
     */
    public static void isPhone(String phone, GlobalErrorCode errorCode) {
        if (!Pattern.compile(RegularConstants.PHONE_REGULAR).matcher(phone).matches())
            throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言字符串不为空
     *
     * @param text 字符串
     */
    public static void isNotEmpty(String text, GlobalErrorCode errorCode) {
        if (text == null || text.trim().length() == 0) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言对象为空
     *
     * @param obj 对象
     */
    public static void isNull(Object obj, GlobalErrorCode errorCode) {
        if (obj != null) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言对象不为空
     *
     * @param obj 对象
     */
    public static void isNotNull(Object obj, GlobalErrorCode errorCode) {
        if (obj == null) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言为假
     *
     * @param isFalse 判断值
     */
    public static void isFalse(boolean isFalse, GlobalErrorCode errorCode) {
        if (isFalse) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言为真
     *
     * @param isTrue 判断值
     */
    public static void isTrue(boolean isTrue, GlobalErrorCode errorCode) {
        if (!isTrue) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言两个字符串一致
     *
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     */
    public static void isEquals(String s1, String s2, GlobalErrorCode errorCode) {
        isNotEmpty(s1);
        isNotEmpty(s2);
        if (!s1.equals(s2)) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言两个字符串一致，去空格
     *
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     */
    public static void isEqualsTrim(String s1, String s2, GlobalErrorCode errorCode) {
        isNotEmpty(s1);
        isNotEmpty(s2);
        if (!s1.trim().equals(s2.trim())) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言两个字符串一致，去空格且忽略大小写
     *
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     */
    public static void isEqualsIgnoreCase(String s1, String s2, GlobalErrorCode errorCode) {
        isNotEmpty(s1);
        isNotEmpty(s2);
        if (!s1.trim().equalsIgnoreCase(s2.trim())) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言集合为空
     *
     * @param list 集合
     */
    public static <T> void listIsNull(List<T> list, GlobalErrorCode errorCode) {
        if (list == null) return;
        else if (list.size() != 0) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言集合不为空
     *
     * @param list 集合
     */
    public static <T> void listIsNotNull(List<T> list, GlobalErrorCode errorCode) {
        if (list == null || list.size() == 0) throwGlobalCustomException(errorCode);
    }
    
    /**
     * 断言时间在之后
     *
     * @param oldDate   旧时间
     * @param newDate   新时间
     * @param errorCode 错误码
     */
    public static void timeIsBefore(Date oldDate, Date newDate, GlobalErrorCode errorCode) {
        if (oldDate.after(newDate)) throwGlobalCustomException(errorCode);
    }
}
