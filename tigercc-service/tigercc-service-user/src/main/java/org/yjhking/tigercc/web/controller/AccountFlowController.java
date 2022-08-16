package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.AccountFlow;
import org.yjhking.tigercc.query.AccountFlowQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IAccountFlowService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/accountFlow")
public class AccountFlowController {
    
    @Resource
    public IAccountFlowService accountFlowService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody AccountFlow accountFlow) {
        if (accountFlow.getId() != null) {
            accountFlowService.updateById(accountFlow);
        } else {
            accountFlowService.insert(accountFlow);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        accountFlowService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(accountFlowService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(accountFlowService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody AccountFlowQuery query) {
        Page<AccountFlow> page = new Page<AccountFlow>(query.getPage(), query.getRows());
        page = accountFlowService.selectPage(page);
        return JsonResult.success(new PageList<AccountFlow>(page.getTotal(), page.getRecords()));
    }
}
