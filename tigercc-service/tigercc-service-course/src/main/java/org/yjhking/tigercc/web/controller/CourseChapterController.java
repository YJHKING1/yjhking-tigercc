package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseChapter;
import org.yjhking.tigercc.query.CourseChapterQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseChapterService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseChapter")
public class CourseChapterController {
    
    @Resource
    public ICourseChapterService courseChapterService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseChapter courseChapter) {
        if (courseChapter.getId() != null) {
            courseChapterService.updateById(courseChapter);
        } else {
            courseChapterService.insert(courseChapter);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseChapterService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseChapterService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseChapterService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseChapterQuery query) {
        Page<CourseChapter> page = new Page<CourseChapter>(query.getPage(), query.getRows());
        page = courseChapterService.selectPage(page);
        return JsonResult.success(new PageList<CourseChapter>(page.getTotal(), page.getRecords()));
    }
}
