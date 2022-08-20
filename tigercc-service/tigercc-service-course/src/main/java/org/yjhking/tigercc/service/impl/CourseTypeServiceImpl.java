package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.CourseType;
import org.yjhking.tigercc.mapper.CourseTypeMapper;
import org.yjhking.tigercc.service.ICourseTypeService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Cacheable(cacheNames = TigerccConstants.COURSE_TYPE_LIST_KEY, key = TigerccConstants.COURSE_TYPE_LIST)
    public List<CourseType> treeData() {
        return tree();
    }
    
    /**
     * 课程类型树
     *
     * @return 课程类型树
     */
    public List<CourseType> tree() {
        List<CourseType> tree = selectList(null);
        Map<Long, CourseType> map = tree.stream()
                .collect(Collectors.toMap(CourseType::getId, CourseType -> CourseType));
        List<CourseType> courseTypeTree = new ArrayList<>();
        tree.forEach(courseType -> {
            if (Objects.equals(courseType.getPid(), NumberConstants.ZERO_LONG)) courseTypeTree.add(courseType);
            else map.get(courseType.getPid()).getChildren().add(courseType);
        });
        return courseTypeTree;
    }
    
    @Override
    @CacheEvict(cacheNames = TigerccConstants.COURSE_TYPE_LIST_KEY, key = TigerccConstants.COURSE_TYPE_LIST)
    public boolean insert(CourseType entity) {
        return super.insert(entity);
    }
    
    @Override
    @CacheEvict(cacheNames = TigerccConstants.COURSE_TYPE_LIST_KEY, key = TigerccConstants.COURSE_TYPE_LIST)
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }
    
    @Override
    @CacheEvict(cacheNames = TigerccConstants.COURSE_TYPE_LIST_KEY, key = TigerccConstants.COURSE_TYPE_LIST)
    public boolean updateById(CourseType entity) {
        return super.updateById(entity);
    }
}
