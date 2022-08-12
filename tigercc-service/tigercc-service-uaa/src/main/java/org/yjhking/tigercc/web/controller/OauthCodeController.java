package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.OauthCode;
import org.yjhking.tigercc.query.OauthCodeQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IOauthCodeService;

@RestController
@RequestMapping("/oauthCode")
public class OauthCodeController {
    
    @Autowired
    public IOauthCodeService oauthCodeService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody OauthCode oauthCode) {
        if (oauthCode.getCode() != null) {
            oauthCodeService.updateById(oauthCode);
        } else {
            oauthCodeService.insert(oauthCode);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        oauthCodeService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(oauthCodeService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(oauthCodeService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody OauthCodeQuery query) {
        Page<OauthCode> page = new Page<OauthCode>(query.getPage(), query.getRows());
        page = oauthCodeService.selectPage(page);
        return JsonResult.success(new PageList<OauthCode>(page.getTotal(), page.getRecords()));
    }
}
