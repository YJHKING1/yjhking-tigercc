package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.IRoleService;
import org.yjhking.tigercc.domain.Role;
import org.yjhking.tigercc.query.RoleQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    public IRoleService roleService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Role role) {
        if (role.getId() != null){
                roleService.updateById(role);
        }else{
                roleService.insert(role);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            roleService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(roleService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(roleService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody RoleQuery query) {
        Page<Role> page = new Page<Role>(query.getPage(), query.getRows());
        page = roleService.selectPage(page);
        return JsonResult.success(new PageList<Role>(page.getTotal(), page.getRecords()));
    }
}
