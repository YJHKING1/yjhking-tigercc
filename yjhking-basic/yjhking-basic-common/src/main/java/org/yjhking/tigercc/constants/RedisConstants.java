package org.yjhking.tigercc.constants;

/**
 * Redis常量类
 *
 * @author YJH
 */
public class RedisConstants {
    /**
     * 手机短信验证码前缀
     */
    public static final String REGISTER_CODE_PREFIX = "sms_register:";
    /**
     * 图片验证码宽度
     */
    public static final Integer CODE_IMG_WIDTH = 115;
    /**
     * 图片验证码高度
     */
    public static final Integer CODE_IMG_HEIGHT = 40;
    /**
     * redis验证符
     */
    public static final String REDIS_VERIFY = ":";
    /**
     * 手机
     */
    public static final String PHONE = "phone";
    /**
     * ip
     */
    public static final String IP = "ip";
    /**
     * 名字
     */
    public static final String NAME = "name";
}
