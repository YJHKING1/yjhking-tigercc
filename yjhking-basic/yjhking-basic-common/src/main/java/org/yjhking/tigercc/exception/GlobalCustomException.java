package org.yjhking.tigercc.exception;

import lombok.Data;
import org.yjhking.tigercc.enums.GlobalErrorCode;

/**
 * 全局自定义异常，调用时必须传入异常码枚举
 */
@Data
public class GlobalCustomException extends RuntimeException {
    
    // 异常信息
    private String errorMessage;
    
    // 异常码
    private String errorCode;
    
    public GlobalCustomException(GlobalErrorCode globalErrorCode) {
        super(globalErrorCode.getMessage());
        this.errorCode = globalErrorCode.getCode();
        this.errorMessage = globalErrorCode.getMessage();
    }
}