package org.yjhking.tigercc.doc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author YJH
 */
@Document(indexName = "course", type = "_doc")
@Data
public class CourseDoc {
    
    @Id
    private Long id;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String name;
    
    @Field(type = FieldType.Keyword)
    private String gradeName;
    
    @Field(type = FieldType.Long)
    private Long courseTypeId;
    
    @Field(type = FieldType.Keyword)
    private String pic;
    
    @Field(type = FieldType.Date)
    private Date onlineTime;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String teacherNames;
    
    @Field(type = FieldType.Integer)
    private Integer charge;
    
    @Field(type = FieldType.Double)
    private BigDecimal price;
    
    @Field(type = FieldType.Double)
    private BigDecimal priceOld;
    
    @Field(type = FieldType.Integer)
    private Integer saleCount;
    
    @Field(type = FieldType.Integer)
    private Integer viewCount;
    
    @Field(type = FieldType.Integer)
    private Integer commentCount;
}
