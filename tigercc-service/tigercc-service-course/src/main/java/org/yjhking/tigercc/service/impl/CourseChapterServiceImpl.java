package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.CourseChapter;
import org.yjhking.tigercc.mapper.CourseChapterMapper;
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

}
