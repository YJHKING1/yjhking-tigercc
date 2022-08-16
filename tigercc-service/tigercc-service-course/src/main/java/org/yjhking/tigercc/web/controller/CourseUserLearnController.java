package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseUserLearn;
import org.yjhking.tigercc.query.CourseUserLearnQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseUserLearnService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseUserLearn")
public class CourseUserLearnController {
    
    @Resource
    public ICourseUserLearnService courseUserLearnService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseUserLearn courseUserLearn) {
        if (courseUserLearn.getId() != null) {
            courseUserLearnService.updateById(courseUserLearn);
        } else {
            courseUserLearnService.insert(courseUserLearn);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseUserLearnService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseUserLearnService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseUserLearnService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseUserLearnQuery query) {
        Page<CourseUserLearn> page = new Page<CourseUserLearn>(query.getPage(), query.getRows());
        page = courseUserLearnService.selectPage(page);
        return JsonResult.success(new PageList<CourseUserLearn>(page.getTotal(), page.getRecords()));
    }
}
