package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.Employee;
import org.yjhking.tigercc.query.EmployeeQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IEmployeeService;

import javax.annotation.Resource;
import javax.validation.Valid;

// @Valid或者@Validated都可以标识需要进行校验
@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
    @Resource
    public IEmployeeService employeeService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody @Valid Employee employee) {
        if (employee.getId() != null) {
            employeeService.updateById(employee);
        } else {
            employeeService.insert(employee);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        employeeService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(employeeService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(employeeService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody EmployeeQuery query) {
        Page<Employee> page = new Page<Employee>(query.getPage(), query.getRows());
        page = employeeService.selectPage(page);
        return JsonResult.success(new PageList<Employee>(page.getTotal(), page.getRecords()));
    }
}
