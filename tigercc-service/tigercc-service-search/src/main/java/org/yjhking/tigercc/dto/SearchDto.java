package org.yjhking.tigercc.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.yjhking.tigercc.query.BaseQuery;

import java.math.BigDecimal;

/**
 * @author YJH
 */
@Data
public class SearchDto extends BaseQuery {
    @Field(type = FieldType.Keyword)
    private String chargeName;
    private Long courseTypeId;
    @Field(type = FieldType.Keyword)
    private String gradeName;
    private BigDecimal priceMax;
    private BigDecimal priceMin;
}
