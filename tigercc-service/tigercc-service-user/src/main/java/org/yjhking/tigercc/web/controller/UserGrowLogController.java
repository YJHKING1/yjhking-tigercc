package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.IUserGrowLogService;
import org.yjhking.tigercc.domain.UserGrowLog;
import org.yjhking.tigercc.query.UserGrowLogQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userGrowLog")
public class UserGrowLogController {

    @Autowired
    public IUserGrowLogService userGrowLogService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UserGrowLog userGrowLog) {
        if (userGrowLog.getId() != null){
                userGrowLogService.updateById(userGrowLog);
        }else{
                userGrowLogService.insert(userGrowLog);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            userGrowLogService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(userGrowLogService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(userGrowLogService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserGrowLogQuery query) {
        Page<UserGrowLog> page = new Page<UserGrowLog>(query.getPage(), query.getRows());
        page = userGrowLogService.selectPage(page);
        return JsonResult.success(new PageList<UserGrowLog>(page.getTotal(), page.getRecords()));
    }
}
