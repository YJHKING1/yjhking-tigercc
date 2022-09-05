package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.KillCourse;
import org.yjhking.tigercc.dto.KillParamDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-09-01
 */
public interface IKillCourseService extends IService<KillCourse> {
    
    void add(KillCourse killCourse);
    
    JsonResult onlineAll();
    
    JsonResult onlineOne(Long killId, Long activityId);
    
    JsonResult kill(KillParamDto dto);
    
    void cancelPreOrder(String orderKey);
}
