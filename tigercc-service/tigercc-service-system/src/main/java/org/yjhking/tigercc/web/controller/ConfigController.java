package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.Config;
import org.yjhking.tigercc.query.ConfigQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IConfigService;

@RestController
@RequestMapping("/config")
@Api(value = "ConfigController", description = "配置")
public class ConfigController {
    
    @Autowired
    public IConfigService configService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "saveOrUpdate", notes = "新增或修改", response = JsonResult.class)
    public JsonResult saveOrUpdate(@ApiParam(name = "config", required = true) @RequestBody Config config) {
        if (config.getId() != null) {
            configService.updateById(config);
        } else {
            configService.insert(config);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        configService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(configService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(configService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody ConfigQuery query) {
        Page<Config> page = new Page<Config>(query.getPage(), query.getRows());
        page = configService.selectPage(page);
        return JsonResult.success(new PageList<Config>(page.getTotal(), page.getRecords()));
    }
}
