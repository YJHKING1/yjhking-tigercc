package org.yjhking.tigercc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRecommendVo {
    /**
     * 课程id
     */
    private Long id;
    /**
     * 课程封面地址
     */
    private String pic;
    /**
     * 课程名
     */
    private String courseName;
}
