package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.CourseChapter;
import org.yjhking.tigercc.mapper.CourseChapterMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ICourseChapterService;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter> implements ICourseChapterService {
    @Override
    public boolean insert(CourseChapter entity) {
        entity.setNumber(selectList(new EntityWrapper<CourseChapter>()
                .eq(TigerccConstants.COURSE_ID, entity.getCourseId())).size() + 1);
        return super.insert(entity);
    }
    
    @Override
    public JsonResult listByCourseId(Long courseId) {
        return JsonResult.success(selectList(new EntityWrapper<CourseChapter>()
                .eq(TigerccConstants.COURSE_ID, courseId)));
    }
}