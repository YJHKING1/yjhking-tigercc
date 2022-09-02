package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.KillCourse;
import org.yjhking.tigercc.query.KillCourseQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IKillCourseService;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/killCourse")
public class KillCourseController {
    
    @Resource
    public IKillCourseService killCourseService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody KillCourse killCourse) {
        if (killCourse.getId() != null) {
            killCourseService.updateById(killCourse);
        } else {
            killCourseService.insert(killCourse);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        killCourseService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(killCourseService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(killCourseService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody KillCourseQuery query) {
        Page<KillCourse> page = new Page<KillCourse>(query.getPage(), query.getRows());
        page = killCourseService.selectPage(page);
        return JsonResult.success(new PageList<KillCourse>(page.getTotal(), page.getRecords()));
    }
    
    @PostMapping("/add")
    public JsonResult add(@RequestBody @Valid KillCourse killCourse) {
        killCourseService.add(killCourse);
        return JsonResult.success();
    }
}
