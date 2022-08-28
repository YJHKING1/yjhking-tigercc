package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseRecommend;
import org.yjhking.tigercc.domain.CourseSummary;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.CourseRecommendMapper;
import org.yjhking.tigercc.service.ICourseRecommendService;
import org.yjhking.tigercc.service.ICourseService;
import org.yjhking.tigercc.service.ICourseSummaryService;
import org.yjhking.tigercc.utils.VerificationUtils;
import org.yjhking.tigercc.vo.CourseRecommendVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-28
 */
@Service
public class CourseRecommendServiceImpl extends ServiceImpl<CourseRecommendMapper, CourseRecommend> implements ICourseRecommendService {
    @Resource
    private ICourseService courseService;
    @Resource
    private ICourseSummaryService courseSummaryService;
    public static final Integer HOT_VIEW_COUNT_NUMBER = 5;
    public static final Boolean DESC = false;
    
    @Override
    public void saveOn(Long id) {
        // 校验课程是否上架
        VerificationUtils.isNotNull(courseService.selectOne(new EntityWrapper<Course>().eq(TigerccConstants.ID, id)
                .and().eq(Course.STATUS, NumberConstants.ONE)), GlobalErrorCode.COURSE_IS_OFF);
        // 激活推荐
        if (VerificationUtils.isValid(selectById(id))) updateById(new CourseRecommend(id, CourseRecommend.STATE_ON));
        else insert(new CourseRecommend(id, CourseRecommend.STATE_ON));
    }
    
    @Override
    public void saveOff(Long id) {
        // 取消推荐
        if (VerificationUtils.isValid(selectById(id))) updateById(new CourseRecommend(id, CourseRecommend.STATE_OFF));
        else insert(new CourseRecommend(id, CourseRecommend.STATE_OFF));
    }
    
    @Override
    public Object selectAll() {
        // 查询所有激活推荐课程id
        List<Long> ids = selectList(new EntityWrapper<CourseRecommend>().eq(CourseRecommend.STATE
                , CourseRecommend.STATE_ON)).stream().map(CourseRecommend::getId).collect(Collectors.toList());
        if (!VerificationUtils.hasLength(ids)) return null;
        // 根据推荐课程id查课程
        List<Course> courses = courseService.selectBatchIds(ids);
        if (!VerificationUtils.hasLength(courses)) return null;
        // 返回集合[{课程id，课程封面地址，课程名},...]
        return courses.stream().map(course -> new CourseRecommendVo(course.getId(), course.getPic(), course.getName()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Object selectHot() {
        // 按照浏览量倒序查询所有课程
        List<CourseSummary> sale_count = courseSummaryService.selectList(new EntityWrapper<CourseSummary>()
                .orderBy(CourseSummary.VIEW_COUNT, DESC));
        VerificationUtils.isHasLength(sale_count, GlobalErrorCode.COURSE_SUMMARY_IS_NULL);
        return courseSummary2CourseRecommendVo(sale_count);
    }
    
    /**
     * 课程概要转换为课程推荐vo
     */
    private List<CourseRecommendVo> courseSummary2CourseRecommendVo(List<CourseSummary> courseSummaries) {
        // 如果数量不足则直接返回
        if (courseSummaries.size() <= HOT_VIEW_COUNT_NUMBER) return courseService.selectBatchIds(courseSummaries
                .stream().map(CourseSummary::getId).collect(Collectors.toList())).stream().map(course ->
                new CourseRecommendVo(course.getId(), course.getPic(), course.getName())).collect(Collectors.toList());
            // 否则取前五个
        else return courseService.selectBatchIds(courseSummaries.subList(NumberConstants.ZERO, HOT_VIEW_COUNT_NUMBER)
                .stream().map(CourseSummary::getId).collect(Collectors.toList())).stream().map(course ->
                new CourseRecommendVo(course.getId(), course.getPic(), course.getName())).collect(Collectors.toList());
    }
    
    /**
     * 获取id集合
     */
    private List<Long> getCourseRecommendIds(List<CourseRecommend> courseRecommends) {
        return courseRecommends.stream().map(CourseRecommend::getId).collect(Collectors.toList());
    }
    
    /**
     * 获取id集合
     */
    private List<Long> getCourseSummaryIds(List<CourseSummary> courseSummaries) {
        return courseSummaries.stream().map(CourseSummary::getId).collect(Collectors.toList());
    }
}
