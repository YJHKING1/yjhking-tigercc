package org.yjhking.tigercc.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author YJH
 */
@Data
public class PayParamDto {
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;
    @NotNull(message = "支付类型不能为空")
    private Integer payType;
    private String callUrl;
}
