package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.IUserRealInfoService;
import org.yjhking.tigercc.domain.UserRealInfo;
import org.yjhking.tigercc.query.UserRealInfoQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userRealInfo")
public class UserRealInfoController {

    @Autowired
    public IUserRealInfoService userRealInfoService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UserRealInfo userRealInfo) {
        if (userRealInfo.getId() != null){
                userRealInfoService.updateById(userRealInfo);
        }else{
                userRealInfoService.insert(userRealInfo);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            userRealInfoService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(userRealInfoService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(userRealInfoService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserRealInfoQuery query) {
        Page<UserRealInfo> page = new Page<UserRealInfo>(query.getPage(), query.getRows());
        page = userRealInfoService.selectPage(page);
        return JsonResult.success(new PageList<UserRealInfo>(page.getTotal(), page.getRecords()));
    }
}
