package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseChapter;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseChapterService extends IService<CourseChapter> {
    
    JsonResult listByCourseId(Long courseId);
}
