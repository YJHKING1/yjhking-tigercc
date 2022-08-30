package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.CourseType;
import org.yjhking.tigercc.dto.CourseTypeCrumbsDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.CourseTypeMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ICourseTypeService;
import org.yjhking.tigercc.utils.AssertUtils;

import java.io.Serializable;
import java.util.*;
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
    
    @Override
    public JsonResult crumbs(Long id) {
        // 查找所有课程分类
        List<CourseType> courseTypes = selectList(null);
        // 查找当前课程分类
        CourseType ownerType = findCourseTypeById(id, courseTypes);
        // 判断当前课程分类是否为空
        AssertUtils.isNotNull(ownerType, GlobalErrorCode.COURSE_TYPE_IS_NULL);
        // 声明返回集合
        List<CourseTypeCrumbsDto> resultList = new ArrayList<>();
        // 遍历当前课程分类的path，并将它们的同级分类添加到返回集合中
        for (Long cid : Arrays.stream(ownerType.getPath().split(TigerccConstants.CRUMBS_PATH_SPLIT))
                .mapToLong(Long::parseLong).toArray()) {
            CourseType courseType = findCourseTypeById(cid, courseTypes);
            resultList.add(new CourseTypeCrumbsDto(courseType, courseTypes.stream().filter(type -> type.getPid()
                    .equals(courseType.getPid())).collect(Collectors.toList())));
        }
        return JsonResult.success(resultList);
    }
    
    /**
     * 通过id查找课程分类
     *
     * @param id          要查找的id
     * @param courseTypes 课程分类列表
     * @return 课程分类
     */
    private CourseType findCourseTypeById(Long id, List<CourseType> courseTypes) {
        return courseTypes.stream().filter(type -> type.getId().equals(id)).findFirst()
                .orElse(null);
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
