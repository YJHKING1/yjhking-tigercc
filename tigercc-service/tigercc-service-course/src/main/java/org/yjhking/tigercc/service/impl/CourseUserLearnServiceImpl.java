package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseUserLearn;
import org.yjhking.tigercc.mapper.CourseUserLearnMapper;
import org.yjhking.tigercc.service.ICourseService;
import org.yjhking.tigercc.service.ICourseUserLearnService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseUserLearnServiceImpl extends ServiceImpl<CourseUserLearnMapper, CourseUserLearn> implements ICourseUserLearnService {
    @Resource
    private ICourseService courseService;
    
    @Override
    public CourseUserLearn selectByCourseOrderNo(String out_trade_no) {
        return selectOne(new EntityWrapper<CourseUserLearn>().eq(CourseUserLearn.COURSE_ORDER_NO, out_trade_no));
    }
    
    @Override
    public void create(Long userId, Long courseId) {
        Course course = courseService.selectById(courseId);
        CourseUserLearn courseUserLearn = new CourseUserLearn();
        courseUserLearn.setStartTime(course.getStartTime());
        courseUserLearn.setEndTime(course.getEndTime());
        courseUserLearn.setState(course.getStatus());
        courseUserLearn.setCourseOrderNo("");
        courseUserLearn.setCreateTime(new Date());
        courseUserLearn.setCourseId(courseId);
        courseUserLearn.setLoginId(userId);
        insert(courseUserLearn);
    }
}
