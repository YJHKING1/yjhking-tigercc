package org.yjhking.tigercc.constants;

/**
 * 验证码常量类
 *
 * @author YJH
 */
public class RedisConstants {
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
    /**
     * 名字
     */
    public static final String NAME = "name";
    public static final String USER_ID = "user_id";
    /**
     * 黑名单缓存key
     */
    public static final String BLACK_LIST_KEY = "black_list_key";
    /**
     * 黑名单缓存
     */
    public static final String BLACK_LIST = "'black_list'";
    /**
     * 课程分类缓存key
     */
    public static final String COURSE_TYPE_LIST_KEY = "course_type_list_key";
    /**
     * 课程分类缓存
     */
    public static final String COURSE_TYPE_LIST = "'course_type_list'";
}
