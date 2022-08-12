package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.ILoginRoleService;
import org.yjhking.tigercc.domain.LoginRole;
import org.yjhking.tigercc.query.LoginRoleQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginRole")
public class LoginRoleController {

    @Autowired
    public ILoginRoleService loginRoleService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody LoginRole loginRole) {
        if (loginRole.getId() != null){
                loginRoleService.updateById(loginRole);
        }else{
                loginRoleService.insert(loginRole);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            loginRoleService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(loginRoleService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(loginRoleService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody LoginRoleQuery query) {
        Page<LoginRole> page = new Page<LoginRole>(query.getPage(), query.getRows());
        page = loginRoleService.selectPage(page);
        return JsonResult.success(new PageList<LoginRole>(page.getTotal(), page.getRecords()));
    }
}
