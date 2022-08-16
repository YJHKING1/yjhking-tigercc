package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseResource;
import org.yjhking.tigercc.query.CourseResourceQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseResourceService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseResource")
public class CourseResourceController {
    
    @Resource
    public ICourseResourceService courseResourceService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseResource courseResource) {
        if (courseResource.getId() != null) {
            courseResourceService.updateById(courseResource);
        } else {
            courseResourceService.insert(courseResource);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseResourceService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseResourceService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseResourceService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseResourceQuery query) {
        Page<CourseResource> page = new Page<CourseResource>(query.getPage(), query.getRows());
        page = courseResourceService.selectPage(page);
        return JsonResult.success(new PageList<CourseResource>(page.getTotal(), page.getRecords()));
    }
}
