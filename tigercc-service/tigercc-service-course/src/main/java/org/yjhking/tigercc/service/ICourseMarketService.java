package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseMarket;
import org.yjhking.tigercc.dto.CourseDto;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseMarketService extends IService<CourseMarket> {
    
    boolean save(CourseDto dto);
}
