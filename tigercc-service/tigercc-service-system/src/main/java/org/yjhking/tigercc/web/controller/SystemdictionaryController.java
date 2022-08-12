package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.ISystemdictionaryService;
import org.yjhking.tigercc.domain.Systemdictionary;
import org.yjhking.tigercc.query.SystemdictionaryQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/systemdictionary")
public class SystemdictionaryController {

    @Autowired
    public ISystemdictionaryService systemdictionaryService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody Systemdictionary systemdictionary) {
        if (systemdictionary.getId() != null){
                systemdictionaryService.updateById(systemdictionary);
        }else{
                systemdictionaryService.insert(systemdictionary);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            systemdictionaryService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(systemdictionaryService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(systemdictionaryService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody SystemdictionaryQuery query) {
        Page<Systemdictionary> page = new Page<Systemdictionary>(query.getPage(), query.getRows());
        page = systemdictionaryService.selectPage(page);
        return JsonResult.success(new PageList<Systemdictionary>(page.getTotal(), page.getRecords()));
    }
}
