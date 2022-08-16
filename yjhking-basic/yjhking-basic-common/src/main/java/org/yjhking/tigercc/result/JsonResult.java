package org.yjhking.tigercc.result;

import lombok.Data;
import org.yjhking.tigercc.enums.GlobalErrorCode;

//返回JSON结果
@Data
//建造者模式
//@Builder
public class JsonResult {
    
    private boolean success = true;
    
    private String message = GlobalErrorCode.SERVICE_OK.getMessage();
    
    //错误码，用来描述错误类型
    private String code = GlobalErrorCode.SERVICE_OK.getCode();
    
    //返回的数据
    private Object data;
    
    /**
     * 创建当前实例
     **/
    public static JsonResult success() {
        return new JsonResult();
    }
    
    /**
     * 创建当前实例
     **/
    public static JsonResult success(Object obj) {
        JsonResult instance = new JsonResult();
        instance.setData(obj);
        return instance;
    }
    
    public static JsonResult success(Object obj, String code) {
        JsonResult instance = new JsonResult();
        instance.setCode(code);
        instance.setData(obj);
        return instance;
    }
    
    /**
     * 创建当前实例
     **/
    public static JsonResult error(String message, String code) {
        JsonResult instance = new JsonResult();
        instance.setMessage(message);
        instance.setSuccess(false);
        instance.setCode(code);
        return instance;
    }
    
    public static JsonResult error() {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(false);
        return jsonResult;
    }
    
    /**
     * 创建当前实例
     **/
    public static JsonResult error(String message) {
        return error(message, null);
    }
    
    public static JsonResult error(GlobalErrorCode globalErrorCode) {
        return error(globalErrorCode.getMessage(), globalErrorCode.getCode());
    }
}
