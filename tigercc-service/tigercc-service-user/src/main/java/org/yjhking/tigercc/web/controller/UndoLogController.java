package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.IUndoLogService;
import org.yjhking.tigercc.domain.UndoLog;
import org.yjhking.tigercc.query.UndoLogQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/undoLog")
public class UndoLogController {

    @Autowired
    public IUndoLogService undoLogService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UndoLog undoLog) {
        if (undoLog.getId() != null){
                undoLogService.updateById(undoLog);
        }else{
                undoLogService.insert(undoLog);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            undoLogService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(undoLogService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(undoLogService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody UndoLogQuery query) {
        Page<UndoLog> page = new Page<UndoLog>(query.getPage(), query.getRows());
        page = undoLogService.selectPage(page);
        return JsonResult.success(new PageList<UndoLog>(page.getTotal(), page.getRecords()));
    }
}
