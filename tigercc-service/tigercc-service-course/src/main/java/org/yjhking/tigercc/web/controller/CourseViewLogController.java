package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseViewLog;
import org.yjhking.tigercc.query.CourseViewLogQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseViewLogService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseViewLog")
public class CourseViewLogController {
    
    @Resource
    public ICourseViewLogService courseViewLogService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseViewLog courseViewLog) {
        if (courseViewLog.getId() != null) {
            courseViewLogService.updateById(courseViewLog);
        } else {
            courseViewLogService.insert(courseViewLog);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseViewLogService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseViewLogService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseViewLogService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseViewLogQuery query) {
        Page<CourseViewLog> page = new Page<CourseViewLog>(query.getPage(), query.getRows());
        page = courseViewLogService.selectPage(page);
        return JsonResult.success(new PageList<CourseViewLog>(page.getTotal(), page.getRecords()));
    }
}
