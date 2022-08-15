package org.yjhking.tigercc.constants;

/**
 * 正则表达式常量
 *
 * @author YJH
 */
public class RegularConstants {
    /**
     * 电话格式校验
     */
    public static final String PHONE_REGULAR = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * 邮箱格式校验
     */
    public static final String EMAIL_REGULAR = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 校验码
     */
    public static final String CHANNEL_REGULAR = "^1$";
}
