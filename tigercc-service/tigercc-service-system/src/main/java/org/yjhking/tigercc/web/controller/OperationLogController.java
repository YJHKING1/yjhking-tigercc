package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.OperationLog;
import org.yjhking.tigercc.query.OperationLogQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IOperationLogService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operationLog")
public class OperationLogController {
    
    @Resource
    public IOperationLogService operationLogService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody OperationLog operationLog) {
        if (operationLog.getId() != null) {
            operationLogService.updateById(operationLog);
        } else {
            operationLogService.insert(operationLog);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        operationLogService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(operationLogService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(operationLogService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody OperationLogQuery query) {
        Page<OperationLog> page = new Page<OperationLog>(query.getPage(), query.getRows());
        page = operationLogService.selectPage(page);
        return JsonResult.success(new PageList<OperationLog>(page.getTotal(), page.getRecords()));
    }
}
