package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.AlipayInfo;
import org.yjhking.tigercc.query.AlipayInfoQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IAlipayInfoService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/alipayInfo")
public class AlipayInfoController {
    
    @Resource
    public IAlipayInfoService alipayInfoService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody AlipayInfo alipayInfo) {
        if (alipayInfo.getId() != null) {
            alipayInfoService.updateById(alipayInfo);
        } else {
            alipayInfoService.insert(alipayInfo);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        alipayInfoService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(alipayInfoService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(alipayInfoService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody AlipayInfoQuery query) {
        Page<AlipayInfo> page = new Page<AlipayInfo>(query.getPage(), query.getRows());
        page = alipayInfoService.selectPage(page);
        return JsonResult.success(new PageList<AlipayInfo>(page.getTotal(), page.getRecords()));
    }
}
