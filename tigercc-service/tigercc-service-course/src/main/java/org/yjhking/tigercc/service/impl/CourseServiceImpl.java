package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseTeacher;
import org.yjhking.tigercc.domain.Teacher;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.CourseMapper;
import org.yjhking.tigercc.service.*;
import org.yjhking.tigercc.utils.VerificationUtils;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    private ICourseDetailService courseDetailService;
    @Resource
    private ICourseMarketService courseMarketService;
    @Resource
    private ICourseResourceService courseResourceService;
    @Resource
    private ICourseSummaryService courseSummaryService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private ICourseTeacherService courseTeacherService;
    
    @Transactional
    @Override
    public void save(CourseDto dto) {
        // 校验数据
        verify(dto);
        // 保存教师
        saveTeacher(dto);
        // 保存课程
        VerificationUtils.isTrue(saveCourse(dto), GlobalErrorCode.COURSE_ERROR);
        // 保存课程详情
        VerificationUtils.isTrue(courseDetailService.save(dto), GlobalErrorCode.COURSE_ERROR);
        // 保存课程营销
        VerificationUtils.isTrue(courseMarketService.save(dto), GlobalErrorCode.COURSE_ERROR);
        // 保存课程资源
        VerificationUtils.isTrue(courseResourceService.save(dto), GlobalErrorCode.COURSE_ERROR);
        // 初始化课程统计
        VerificationUtils.isTrue(courseSummaryService.save(dto.getCourse().getId()), GlobalErrorCode.COURSE_ERROR);
        // 保存教师中间表
        saveCourseTeacher(dto);
    }
    
    /**
     * 保存教师
     *
     * @param dto 课程数据
     */
    private void saveTeacher(CourseDto dto) {
        dto.getCourse().setTeacherNames(teacherService.selectBatchIds(dto.getTeacharIds()).stream()
                .map(Teacher::getName).collect(Collectors.joining("，")));
    }
    
    /**
     * 保存课程
     *
     * @param dto 课程数据
     */
    private void saveCourseTeacher(CourseDto dto) {
        dto.getTeacharIds().forEach(teacherId -> {
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setCourseId(dto.getCourse().getId());
            courseTeacher.setTeacherId(teacherId);
            courseTeacherService.insert(courseTeacher);
        });
    }
    
    /**
     * 保存课程
     *
     * @param dto 课程数据
     * @return 是否保存成功
     */
    private boolean saveCourse(CourseDto dto) {
        Course course = dto.getCourse();
        course.setStatus(NumberConstants.ZERO);
        course.setChapterCount(NumberConstants.ZERO);
        return insert(course);
    }
    
    /**
     * 校验数据
     *
     * @param dto 课程数据
     */
    private void verify(CourseDto dto) {
        // 课程名重复校验
        VerificationUtils.listIsNull(selectList(new EntityWrapper<Course>()
                .eq(RedisConstants.NAME, dto.getCourse().getName())), GlobalErrorCode.COURSE_NAME_REPEAT);
        // 校验课程时间
        VerificationUtils.timeIsBefore(dto.getCourse().getStartTime(), dto.getCourse().getEndTime()
                , GlobalErrorCode.COURSE_TIME_ERROR);
    }
}
