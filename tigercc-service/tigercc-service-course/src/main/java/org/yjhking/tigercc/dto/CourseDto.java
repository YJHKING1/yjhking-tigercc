package org.yjhking.tigercc.dto;

import lombok.Data;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseDetail;
import org.yjhking.tigercc.domain.CourseMarket;
import org.yjhking.tigercc.domain.CourseResource;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author YJH
 */
@Data
public class CourseDto {
    @Valid
    private Course course;
    @Valid
    private CourseDetail courseDetail;
    @Valid
    private CourseMarket courseMarket;
    @Valid
    private CourseResource courseResource;
    @NotEmpty(message = "教师不能为空")
    private List<Long> teacharIds;
}
