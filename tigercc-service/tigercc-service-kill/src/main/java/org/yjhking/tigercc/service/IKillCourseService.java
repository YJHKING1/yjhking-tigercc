package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.KillCourse;

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
}
