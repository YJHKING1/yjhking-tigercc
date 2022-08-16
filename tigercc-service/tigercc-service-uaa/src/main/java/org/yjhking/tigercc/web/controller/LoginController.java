package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.Login;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.query.LoginQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ILoginService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    @Resource
    public ILoginService loginService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Login login) {
        if (login.getId() != null) {
            loginService.updateById(login);
        } else {
            loginService.insert(login);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        loginService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(loginService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(loginService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody LoginQuery query) {
        Page<Login> page = new Page<Login>(query.getPage(), query.getRows());
        page = loginService.selectPage(page);
        return JsonResult.success(new PageList<Login>(page.getTotal(), page.getRecords()));
    }
    
    /**
     * 新增
     */
    @PostMapping("/insert")
    public JsonResult insert(@RequestBody RegisterDto dto) {
        return loginService.save(dto);
    }
}
