package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseType;
import org.yjhking.tigercc.query.CourseTypeQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseTypeService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseType")
public class CourseTypeController {
    
    @Resource
    public ICourseTypeService courseTypeService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseType courseType) {
        if (courseType.getId() != null) {
            courseTypeService.updateById(courseType);
        } else {
            courseTypeService.insert(courseType);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseTypeService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseTypeService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseTypeService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseTypeQuery query) {
        Page<CourseType> page = new Page<CourseType>(query.getPage(), query.getRows());
        page = courseTypeService.selectPage(page);
        return JsonResult.success(new PageList<CourseType>(page.getTotal(), page.getRecords()));
    }
    
    /**
     * 课程树
     */
    @GetMapping("/treeData")
    public JsonResult treeData() {
        return JsonResult.success(courseTypeService.treeData());
    }
    
    @GetMapping("/crumbs/{id}")
    public JsonResult crumbs(@PathVariable Long id) {
        return courseTypeService.crumbs(id);
    }
}
