package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.query.CourseQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseService;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/course")
public class CourseController {
    
    @Resource
    public ICourseService courseService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody @Valid CourseDto dto) {
        courseService.save(dto);
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseQuery query) {
        Page<Course> page = new Page<Course>(query.getPage(), query.getRows());
        page = courseService.selectPage(page, new EntityWrapper<Course>()
                .like(RedisConstants.NAME, query.getKeyword()));
        return JsonResult.success(new PageList<Course>(page.getTotal(), page.getRecords()));
    }
    
    /**
     * 上架课程
     */
    @PostMapping("/onLineCourse/{id}")
    public JsonResult onLineCourse(@PathVariable("id") Long id) {
        return courseService.onLineCourse(id);
    }
    
    /**
     * 下架课程
     */
    @PostMapping("/offLineCourse/{id}")
    public JsonResult offLineCourse(@PathVariable("id") Long id) {
        return courseService.offLineCourse(id);
    }
    
    @GetMapping("/detail/data/{id}")
    public JsonResult selectCourseDataForDetail(@PathVariable("id") Long id) {
        return courseService.selectCourseDataForDetail(id);
    }
    
    @GetMapping("/selectCourse/{id}")
    public JsonResult selectCourseStatusForUser(@PathVariable("id") Long id) {
        return courseService.selectCourseStatusForUser(id);
    }
    
    @GetMapping("/info/{courseIds}")
    public JsonResult selectCourseDataForOrder(@PathVariable("courseIds") String courseIds) {
        return courseService.selectCourseDataForOrder(courseIds);
    }
    
    /**
     * 课程推荐
     */
    @GetMapping("/recommend/on/{id}")
    public JsonResult recommendOn(@PathVariable Long id) {
        return courseService.recommendOn(id);
    }
    
    /**
     * 取消课程推荐
     */
    @GetMapping("/recommend/off/{id}")
    public JsonResult recommendOff(@PathVariable Long id) {
        return courseService.recommendOff(id);
    }
}
