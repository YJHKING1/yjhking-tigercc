package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseTeacher;
import org.yjhking.tigercc.query.CourseTeacherQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseTeacherService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseTeacher")
public class CourseTeacherController {
    
    @Resource
    public ICourseTeacherService courseTeacherService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseTeacher courseTeacher) {
        if (courseTeacher.getId() != null) {
            courseTeacherService.updateById(courseTeacher);
        } else {
            courseTeacherService.insert(courseTeacher);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseTeacherService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseTeacherService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseTeacherService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseTeacherQuery query) {
        Page<CourseTeacher> page = new Page<CourseTeacher>(query.getPage(), query.getRows());
        page = courseTeacherService.selectPage(page);
        return JsonResult.success(new PageList<CourseTeacher>(page.getTotal(), page.getRecords()));
    }
}
