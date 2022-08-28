package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseRecommend;
import org.yjhking.tigercc.query.CourseRecommendQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseRecommendService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseRecommend")
public class CourseRecommendController {
    
    @Resource
    public ICourseRecommendService courseRecommendService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseRecommend courseRecommend) {
        if (courseRecommend.getId() != null) {
            courseRecommendService.updateById(courseRecommend);
        } else {
            courseRecommendService.insert(courseRecommend);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseRecommendService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseRecommendService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseRecommendService.selectList(null));
    }
    
    /**
     * 查询课程推荐
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult all() {
        return JsonResult.success(courseRecommendService.selectAll());
    }
    
    /**
     * 查询热门推荐
     */
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public JsonResult hot() {
        return JsonResult.success(courseRecommendService.selectHot());
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseRecommendQuery query) {
        Page<CourseRecommend> page = new Page<CourseRecommend>(query.getPage(), query.getRows());
        page = courseRecommendService.selectPage(page);
        return JsonResult.success(new PageList<CourseRecommend>(page.getTotal(), page.getRecords()));
    }
}
