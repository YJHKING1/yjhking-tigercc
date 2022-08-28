package org.yjhking.tigercc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayOrder2MQDto {
    private BigDecimal amount;
    private Integer payType;
    private String orderNo;
    private Long userId;
    private String extParams;
    private String subject;
}
