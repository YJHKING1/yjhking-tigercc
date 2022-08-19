package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseResource;
import org.yjhking.tigercc.dto.CourseDto;

/**
 * <p>
 * 课件 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseResourceService extends IService<CourseResource> {
    
    boolean save(CourseDto dto);
}
