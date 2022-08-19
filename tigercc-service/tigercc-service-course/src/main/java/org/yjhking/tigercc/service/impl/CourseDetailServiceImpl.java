package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.CourseDetail;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.mapper.CourseDetailMapper;
import org.yjhking.tigercc.service.ICourseDetailService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseDetailServiceImpl extends ServiceImpl<CourseDetailMapper, CourseDetail> implements ICourseDetailService {
    
    @Override
    public boolean save(CourseDto dto) {
        dto.getCourseDetail().setId(dto.getCourse().getId());
        return insert(dto.getCourseDetail());
    }
}
