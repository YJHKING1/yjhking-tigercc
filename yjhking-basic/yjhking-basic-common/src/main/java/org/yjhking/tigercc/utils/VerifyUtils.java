package org.yjhking.tigercc.utils;

import java.util.List;

/**
 * 校验工具类
 *
 * @author YJH
 */
public class VerifyUtils {
    /**
     * 对象非空校验
     *
     * @param obj 对象
     * @return true:非空 false:空
     */
    public static boolean nonEmpty(Object obj) {
        return obj != null;
    }
    
    /**
     * 对象为空校验
     *
     * @param obj 对象
     * @return true:空 false:非空
     */
    public static boolean empty(Object obj) {
        return !nonEmpty(obj);
    }
    
    /**
     * 对象非空等值校验
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return true:相等 false:不相等
     */
    public static boolean equals(Object o1, Object o2) {
        if (!nonEmpty(o1)) return false;
        if (!nonEmpty(o2)) return false;
        return o1.equals(o2);
    }
    
    /**
     * 字符串非空校验
     *
     * @param str 字符串
     * @return true:非空 false:空
     */
    public static boolean hasLength(String str) {
        return str != null && str.trim().length() > 0;
    }
    
    /**
     * list集合非空校验
     *
     * @param list list集合
     * @param <T>  对象类型
     * @return true:非空 false:空
     */
    public static <T> boolean hasLength(List<T> list) {
        return list != null && list.size() > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @param <T>   数组类型
     * @return true:非空 false:空
     */
    public static <T> boolean hasLength(T[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(byte[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(short[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(int[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(long[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(float[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(double[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(boolean[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 数组非空校验
     *
     * @param array 数组
     * @return true:非空 false:空
     */
    public static boolean hasLength(char[] array) {
        return array != null && array.length > 0;
    }
}
