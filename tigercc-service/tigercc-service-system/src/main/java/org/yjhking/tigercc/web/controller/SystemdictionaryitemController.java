package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.Systemdictionaryitem;
import org.yjhking.tigercc.query.SystemdictionaryitemQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ISystemdictionaryitemService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/systemdictionaryitem")
public class SystemdictionaryitemController {
    
    @Resource
    public ISystemdictionaryitemService systemdictionaryitemService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Systemdictionaryitem systemdictionaryitem) {
        if (systemdictionaryitem.getId() != null) {
            systemdictionaryitemService.updateById(systemdictionaryitem);
        } else {
            systemdictionaryitemService.insert(systemdictionaryitem);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        systemdictionaryitemService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(systemdictionaryitemService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(systemdictionaryitemService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody SystemdictionaryitemQuery query) {
        Page<Systemdictionaryitem> page = new Page<Systemdictionaryitem>(query.getPage(), query.getRows());
        page = systemdictionaryitemService.selectPage(page);
        return JsonResult.success(new PageList<Systemdictionaryitem>(page.getTotal(), page.getRecords()));
    }
}
