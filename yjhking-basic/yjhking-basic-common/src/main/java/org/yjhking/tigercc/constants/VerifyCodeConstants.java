package org.yjhking.tigercc.constants;

/**
 * 验证码常量类
 *
 * @author YJH
 */
public class VerifyCodeConstants {
    /**
     * 手机短信验证码注册业务键前缀
     */
    public static final String REGISTER_CODE_PREFIX = "register:";
    /**
     * 微信绑定验证码前缀
     */
    public static final String BINDER_CODE_PREFIX = "register:";
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
     * redis验证分割第一段
     */
    public static final Integer REDIS_VERIFY_FIRST = 0;
    /**
     * redis验证分割第二段
     */
    public static final Integer REDIS_VERIFY_SECOND = 1;
    /**
     * 手机
     */
    public static final String PHONE = "phone";
    /**
     * ip
     */
    public static final String IP = "ip";
    public static final String USER_ID = "user_id";
}
