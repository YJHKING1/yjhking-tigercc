package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseCollect;
import org.yjhking.tigercc.query.CourseCollectQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseCollectService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseCollect")
public class CourseCollectController {
    
    @Resource
    public ICourseCollectService courseCollectService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseCollect courseCollect) {
        if (courseCollect.getId() != null) {
            courseCollectService.updateById(courseCollect);
        } else {
            courseCollectService.insert(courseCollect);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseCollectService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseCollectService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseCollectService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseCollectQuery query) {
        Page<CourseCollect> page = new Page<CourseCollect>(query.getPage(), query.getRows());
        page = courseCollectService.selectPage(page);
        return JsonResult.success(new PageList<CourseCollect>(page.getTotal(), page.getRecords()));
    }
}
