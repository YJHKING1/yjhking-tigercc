package org.yjhking.tigercc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yjhking.tigercc.domain.*;

import java.util.List;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDataForDetailVO {
    private Course course;
    private CourseMarket courseMarket;
    private CourseDetail courseDetail;
    private CourseSummary courseSummary;
    private List<Teacher> teachers;
    private List<CourseChapter> courseChapters;
}
