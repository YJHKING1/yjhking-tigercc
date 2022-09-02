package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.KillActivity;
import org.yjhking.tigercc.query.KillActivityQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IKillActivityService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/killActivity")
public class KillActivityController {
    
    @Resource
    public IKillActivityService killActivityService;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody KillActivity killActivity) {
        if (killActivity.getId() != null) {
            killActivityService.updateById(killActivity);
        } else {
            killActivity.setCreateTime(new Date());
            killActivity.setPublishStatus(KillActivity.STATUS_WAIT_PUBLISH);
            killActivity.setTimeStr(sdf.format(killActivity.getStartTime()));
            killActivityService.insert(killActivity);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        killActivityService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(killActivityService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(killActivityService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody KillActivityQuery query) {
        Page<KillActivity> page = new Page<KillActivity>(query.getPage(), query.getRows());
        page = killActivityService.selectPage(page);
        return JsonResult.success(new PageList<KillActivity>(page.getTotal(), page.getRecords()));
    }
    
    @GetMapping("/publish/{id}")
    public JsonResult start(@PathVariable("id") Long id) {
        killActivityService.publish(id);
        return JsonResult.success();
    }
}
