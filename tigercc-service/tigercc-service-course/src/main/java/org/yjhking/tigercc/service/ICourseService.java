package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseService extends IService<Course> {
    
    void save(CourseDto dto);
    
    JsonResult onLineCourse(Long id);
    
    JsonResult offLineCourse(Long id);
    
    JsonResult selectCourseDataForDetail(Long id);
    JsonResult selectCourseStatusForUser(Long id);
    
    JsonResult selectCourseDataForOrder(String courseIds);
}
