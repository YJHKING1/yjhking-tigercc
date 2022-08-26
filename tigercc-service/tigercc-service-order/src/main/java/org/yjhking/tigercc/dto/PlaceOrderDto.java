package org.yjhking.tigercc.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author YJH
 */
@Data
public class PlaceOrderDto {
    @NotEmpty(message = "非法请求")
    private List<Long> courseIds;
    @NotNull(message = "请选择支付方式")
    private Integer payType;
    @NotEmpty(message = "非法请求")
    private String token;
    @NotNull(message = "非法请求")
    private Integer type;
}
