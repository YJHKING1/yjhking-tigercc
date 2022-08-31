package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseUserLearn;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseUserLearnService extends IService<CourseUserLearn> {
    
    CourseUserLearn selectByCourseOrderNo(String out_trade_no);
    
    void create(Long userId, Long courseId);
}
