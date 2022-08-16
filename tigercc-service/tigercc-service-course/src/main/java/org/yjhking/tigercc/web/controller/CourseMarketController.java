package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseMarket;
import org.yjhking.tigercc.query.CourseMarketQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseMarketService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseMarket")
public class CourseMarketController {
    
    @Resource
    public ICourseMarketService courseMarketService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseMarket courseMarket) {
        if (courseMarket.getId() != null) {
            courseMarketService.updateById(courseMarket);
        } else {
            courseMarketService.insert(courseMarket);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseMarketService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseMarketService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseMarketService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseMarketQuery query) {
        Page<CourseMarket> page = new Page<CourseMarket>(query.getPage(), query.getRows());
        page = courseMarketService.selectPage(page);
        return JsonResult.success(new PageList<CourseMarket>(page.getTotal(), page.getRecords()));
    }
}
