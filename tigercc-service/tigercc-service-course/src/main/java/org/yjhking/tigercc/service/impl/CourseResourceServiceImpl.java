package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.CourseResource;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.mapper.CourseResourceMapper;
import org.yjhking.tigercc.service.ICourseResourceService;

/**
 * <p>
 * 课件 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseResourceServiceImpl extends ServiceImpl<CourseResourceMapper, CourseResource> implements ICourseResourceService {
    
    @Override
    public boolean save(CourseDto dto) {
        CourseResource courseResource = dto.getCourseResource();
        courseResource.setId(dto.getCourse().getId());
        courseResource.setCourseId(dto.getCourse().getId());
        return insert(courseResource);
    }
}
