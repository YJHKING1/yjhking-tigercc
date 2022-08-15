package org.yjhking.tigercc.enums;

/**
 * 状态码枚举
 * 公共异常：100xx；
 * system服务异常：200xx；
 * uaa服务异常：300xx；
 * user服务异常：400xx
 */
public enum GlobalErrorCode {
    // 成功
    SERVICE_OK("0", "服务正常！"),
    // 失败
    SERVICE_ERROR("-1", "服务不可用，请稍后重试！"),
    // 公共异常：100xx
    SERVICE_PARAM_ERROR("10000", "业务异常！"),
    SERVICE_PARAM_IS_NULL("10001", "参数不能为空！"),
    // system服务异常：200xx
    SYSTEM_ERROR("20000", "系统服务异常！"),
    // uaa服务异常：300xx
    UAA_ERROR("30000", "认证服务异常异常！"),
    // user服务异常：400xx
    USER_ERROR("40000", "用户服务异常！"),
    USER_USERNAME_ERROR("40001", "用户名称不合法！");
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