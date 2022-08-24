package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseType;
import org.yjhking.tigercc.result.JsonResult;

import java.util.List;

/**
 * <p>
 * 课程目录 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseTypeService extends IService<CourseType> {
    List<CourseType> treeData();
    
    JsonResult crumbs(Long id);
}
