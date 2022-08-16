package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.CourseTeacher;
import org.yjhking.tigercc.mapper.CourseTeacherMapper;
import org.yjhking.tigercc.service.ICourseTeacherService;

/**
 * <p>
 * 课程和老师的中间表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {

}
