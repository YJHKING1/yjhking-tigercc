package org.yjhking.tigercc.dto;

import lombok.Data;
import org.yjhking.tigercc.query.BaseQuery;

import java.math.BigDecimal;

/**
 * @author YJH
 */
@Data
public class SearchDto extends BaseQuery {
    private String chargeName;
    private Long courseTypeId;
    private String gradeName;
    private BigDecimal priceMax;
    private BigDecimal priceMin;
}
