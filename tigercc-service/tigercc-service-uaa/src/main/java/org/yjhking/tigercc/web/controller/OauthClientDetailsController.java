package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.OauthClientDetails;
import org.yjhking.tigercc.query.OauthClientDetailsQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IOauthClientDetailsService;

@RestController
@RequestMapping("/oauthClientDetails")
public class OauthClientDetailsController {
    
    @Autowired
    public IOauthClientDetailsService oauthClientDetailsService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody OauthClientDetails oauthClientDetails) {
        if (oauthClientDetails.getClientId() != null) {
            oauthClientDetailsService.updateById(oauthClientDetails);
        } else {
            oauthClientDetailsService.insert(oauthClientDetails);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        oauthClientDetailsService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(oauthClientDetailsService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(oauthClientDetailsService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody OauthClientDetailsQuery query) {
        Page<OauthClientDetails> page = new Page<OauthClientDetails>(query.getPage(), query.getRows());
        page = oauthClientDetailsService.selectPage(page);
        return JsonResult.success(new PageList<OauthClientDetails>(page.getTotal(), page.getRecords()));
    }
}
