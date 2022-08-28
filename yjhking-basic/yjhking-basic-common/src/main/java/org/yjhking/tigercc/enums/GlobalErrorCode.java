package org.yjhking.tigercc.enums;

/**
 * 状态码枚举
 * 公共异常：100xx；
 * system服务异常：200xx；
 * uaa服务异常：300xx；
 * user服务异常：400xx
 * common服务异常：500xx
 */
public enum GlobalErrorCode {
    // 成功
    SERVICE_OK("0", "服务正常！"),
    // 失败
    SERVICE_ERROR("-1", "服务不可用，请稍后重试！"),
    
    // 公共异常：100xx
    SERVICE_PARAM_ERROR("10000", "业务异常！"),
    SERVICE_PARAM_IS_NULL("10001", "参数不能为空！"),
    SERVICE_OBJECT_IS_NULL("10002", "对象不能为空！"),
    SERVICE_OBJECT_IS_NOT_NULL("10003", "对象不为空！"),
    SERVICE_PARAM_IS_FALSE("10004", "参数为假！"),
    SERVICE_PARAM_IS_TRUE("10005", "参数为真！"),
    SERVICE_PARAM_IS_NOT_EQUALS("10006", "字符串不一致！"),
    SERVICE_IP_ERROR("10007", "IP异常！"),
    SERVICE_LIST_IS_NOT_NULL("10008", "集合不为空！"),
    SERVICE_LIST_IS_NULL("10009", "集合为空！"),
    SERVICE_ILLEGAL_REQUEST("10010", "非法请求！"),
    
    // system服务异常：200xx
    SYSTEM_ERROR("20000", "系统服务异常！"),
    
    // uaa服务异常：300xx
    UAA_ERROR("30000", "认证服务异常异常！"),
    
    // user服务异常：400xx
    USER_ERROR("40000", "用户服务异常！"),
    USER_USERNAME_ERROR("40001", "用户名称不合法！"),
    USER_PHONE_REPEAT_ERROR("40002", "手机已注册！"),
    USER_PHONE_FORMAT_ERROR("40003", "手机格式错误！"),
    USER_VERIFICATION_BLACK("40004", "您已被拉黑！"),
    USER_IS_NULL("40005", "用户不存在！"),
    USER_PASSWORD_IS_ERROR("40006", "密码错误！"),
    
    // common服务异常：500xx
    COMMON_VERIFICATION_ERROR("50001", "验证码创建失败！"),
    COMMON_VERIFICATION_REPEAT_SEND("50002", "请勿重发验证码！"),
    COMMON_PHONE_VERIFICATION_OVERDUE("50003", "手机验证码过期！"),
    COMMON_PHONE_VERIFICATION_ERROR("50004", "手机验证码错误！"),
    COMMON_IMG_VERIFICATION_NULL("50005", "图片验证码为空！"),
    COMMON_IMG_VERIFICATION_ERROR("50006", "图片验证码错误！"),
    COMMON_IMG_VERIFICATION_OVERDUE("50007", "图片验证码过期！"),
    COMMON_VERIFICATION_BLACK("50008", "该手机号或IP已被拉黑，请勿重复操作！"),
    
    // course服务异常：600xx
    COURSE_ERROR("60000", "课程服务异常！"),
    COURSE_TIME_ERROR("60001", "课程时间错误！"),
    COURSE_NAME_REPEAT("60002", "课程名重复！"),
    COURSE_PRICE_IS_NULL("60002", "课程价格不能为空！"),
    COURSE_TYPE_IS_NULL("60003", "课程分类不存在！"),
    COURSE_IS_NOT_ONLINE("60004", "课程未上线！"),
    COURSE_ID_IS_NULL("60005", "课程ID不能为空！"),
    COURSE_IS_NULL("60006", "课程为空！"),
    COURSE_IS_NOT_BUY("60007", "课程未购买！"),
    COURSE_IS_FREE("60008", "课程免费！"),
    COURSE_IS_OFF("60009", "课程已下架不可推荐！"),
    COURSE_SUMMARY_IS_NULL("60010", "可推荐课程为空！"),
    
    // search服务异常：700xx
    SEARCH_ERROR("70000", "搜索服务异常！"),
    
    // media服务异常：800xx
    MEDIA_ERROR("80000", "媒体服务异常！"),
    MEDIA_LIST_NULL("80001", "视频列表加载失败！"),
    
    // order服务异常：900xx
    ORDER_ERROR("90000", "订单服务异常！"),
    ORDER_REPEAT("90001", "订单重复提交，或下单超时！"),
    ORDER_EXIST("90002", "订单已存在！");
    
    // 异常码
    private String code;
    // 异常信息
    private String message;
    
    private GlobalErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}