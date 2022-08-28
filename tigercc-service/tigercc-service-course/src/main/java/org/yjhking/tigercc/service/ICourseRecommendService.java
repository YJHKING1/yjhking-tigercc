package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseRecommend;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-28
 */
public interface ICourseRecommendService extends IService<CourseRecommend> {
    
    void saveOn(Long id);
    
    void saveOff(Long id);
    
    Object selectAll();
    
    Object selectHot();
}
