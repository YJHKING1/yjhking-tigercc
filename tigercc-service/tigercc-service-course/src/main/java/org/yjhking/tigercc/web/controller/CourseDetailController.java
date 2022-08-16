package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseDetail;
import org.yjhking.tigercc.query.CourseDetailQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseDetailService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseDetail")
public class CourseDetailController {
    
    @Resource
    public ICourseDetailService courseDetailService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseDetail courseDetail) {
        if (courseDetail.getId() != null) {
            courseDetailService.updateById(courseDetail);
        } else {
            courseDetailService.insert(courseDetail);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseDetailService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseDetailService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseDetailService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseDetailQuery query) {
        Page<CourseDetail> page = new Page<CourseDetail>(query.getPage(), query.getRows());
        page = courseDetailService.selectPage(page);
        return JsonResult.success(new PageList<CourseDetail>(page.getTotal(), page.getRecords()));
    }
}
