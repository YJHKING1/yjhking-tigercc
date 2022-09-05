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
public class PreCourseOrder2RedisDto {
    private BigDecimal amount;
    private Integer count;
    private Long courseId;
    private String courseName;
    private String coursePic;
    private String courseNo;
    private Long killCourseId;
    private Long userId;
}
