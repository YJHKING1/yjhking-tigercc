package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.CourseType;
import org.yjhking.tigercc.mapper.CourseTypeMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ICourseTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程目录 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {
    
    @Override
    public JsonResult treeData() {
        List<CourseType> tree = selectList(null);
        Map<Long, CourseType> map = tree.stream()
                .collect(Collectors.toMap(CourseType::getId, CourseType -> CourseType));
        List<CourseType> petTypeTree = new ArrayList<>();
        for (CourseType courseType : tree) {
            if (courseType.getPid() == 0L) {
                petTypeTree.add(courseType);
            } else {
                CourseType parentType = map.get(courseType.getPid());
                parentType.getChildren().add(courseType);
            }
        }
        return JsonResult.success(petTypeTree);
    }
}
