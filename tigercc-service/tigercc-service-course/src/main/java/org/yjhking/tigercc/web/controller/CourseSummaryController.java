package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseSummary;
import org.yjhking.tigercc.query.CourseSummaryQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseSummaryService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseSummary")
public class CourseSummaryController {
    
    @Resource
    public ICourseSummaryService courseSummaryService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseSummary courseSummary) {
        if (courseSummary.getId() != null) {
            courseSummaryService.updateById(courseSummary);
        } else {
            courseSummaryService.insert(courseSummary);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseSummaryService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseSummaryService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseSummaryService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseSummaryQuery query) {
        Page<CourseSummary> page = new Page<CourseSummary>(query.getPage(), query.getRows());
        page = courseSummaryService.selectPage(page);
        return JsonResult.success(new PageList<CourseSummary>(page.getTotal(), page.getRecords()));
    }
}
