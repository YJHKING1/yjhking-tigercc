package org.yjhking.tigercc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.result.JsonResult;

import java.util.List;

/**
 * @author YJH
 */
// 全局统一异常处理，放回Json格式
@RestControllerAdvice
// 日志
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 全局系统异常处理
     */
    @ExceptionHandler(Exception.class)
    public JsonResult exceptionHandler(Exception e) {
        e.printStackTrace();
        // 打印日志
        log.error("系统异常：" + e.getMessage());
        return JsonResult.error(GlobalErrorCode.SERVICE_ERROR);
    }
    
    /**
     * 业务异常
     */
    @ExceptionHandler(GlobalCustomException.class)
    public JsonResult GlobalCustomExceptionHandler(GlobalCustomException e) {
        e.printStackTrace();
        // 打印日志
        log.error("发生业务异常，异常码：{}, 异常信息：{}", e.getErrorCode(), e.getErrorMessage());
        return JsonResult.error(e.getErrorMessage(), e.getErrorCode());
    }
    
    /**
     * 全局参数异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        e.printStackTrace();
        // 打印日志
        log.error("发生方法参数校验异常，异常信息为：" + e.getMessage());
        // 返回异常信息，异常信息我们需要获取到我们自定义的字段提示语
        // 得到所有的字段异常提示信息
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuffer sbf = new StringBuffer();
        sbf.append("参数校验失败，失败原因为：");
        // 遍历得到所有异常提示语
        allErrors.forEach(objectError -> sbf.append(objectError.getDefaultMessage()).append("；"));
        return JsonResult.error(sbf.toString());
    }
}