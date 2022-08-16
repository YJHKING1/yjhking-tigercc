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
    
    // system服务异常：200xx
    SYSTEM_ERROR("20000", "系统服务异常！"),
    
    // uaa服务异常：300xx
    UAA_ERROR("30000", "认证服务异常异常！"),
    
    // user服务异常：400xx
    USER_ERROR("40000", "用户服务异常！"),
    USER_USERNAME_ERROR("40001", "用户名称不合法！"),
    USER_PHONE_REPEAT_ERROR("40002", "手机已注册！"),
    USER_PHONE_FORMAT_ERROR("40003", "手机格式错误！"),
    
    // common服务异常：500xx
    COMMON_VERIFICATION_ERROR("50001", "验证码创建失败！"),
    COMMON_VERIFICATION_REPEAT_SEND("50002", "请勿重发验证码！"),
    COMMON_PHONE_VERIFICATION_OVERDUE("50003", "手机验证码过期！"),
    COMMON_PHONE_VERIFICATION_ERROR("50004", "手机验证码错误！"),
    COMMON_IMG_VERIFICATION_NULL("50005", "图片验证码为空！"),
    COMMON_IMG_VERIFICATION_ERROR("50006", "图片验证码错误！"),
    COMMON_IMG_VERIFICATION_OVERDUE("50007", "图片验证码过期！");
    
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