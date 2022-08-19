package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseOrderItem;
import org.yjhking.tigercc.query.CourseOrderItemQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseOrderItemService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseOrderItem")
public class CourseOrderItemController {
    
    @Resource
    public ICourseOrderItemService courseOrderItemService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseOrderItem courseOrderItem) {
        if (courseOrderItem.getId() != null) {
            courseOrderItemService.updateById(courseOrderItem);
        } else {
            courseOrderItemService.insert(courseOrderItem);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseOrderItemService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseOrderItemService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseOrderItemService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseOrderItemQuery query) {
        Page<CourseOrderItem> page = new Page<CourseOrderItem>(query.getPage(), query.getRows());
        page = courseOrderItemService.selectPage(page);
        return JsonResult.success(new PageList<CourseOrderItem>(page.getTotal(), page.getRecords()));
    }
}
