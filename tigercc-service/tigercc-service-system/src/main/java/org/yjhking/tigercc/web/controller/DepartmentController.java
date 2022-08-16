package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.Department;
import org.yjhking.tigercc.query.DepartmentQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IDepartmentService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    
    @Resource
    public IDepartmentService departmentService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Department department) {
        if (department.getId() != null) {
            departmentService.updateById(department);
        } else {
            departmentService.insert(department);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(departmentService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(departmentService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody DepartmentQuery query) {
        Page<Department> page = new Page<Department>(query.getPage(), query.getRows());
        page = departmentService.selectPage(page);
        return JsonResult.success(new PageList<Department>(page.getTotal(), page.getRecords()));
    }
}
