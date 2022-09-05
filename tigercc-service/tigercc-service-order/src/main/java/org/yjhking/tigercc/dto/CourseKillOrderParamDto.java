package org.yjhking.tigercc.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author YJH
 */
@Data
public class CourseKillOrderParamDto {
    @NotNull(message = "请选择支付方式")
    private Integer payType;
    @NotEmpty(message = "非法请求")
    private String token;
    @NotEmpty(message = "非法请求")
    private String orderNo;
}
