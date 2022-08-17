package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.MessageBlack;
import org.yjhking.tigercc.dto.BlackDto;
import org.yjhking.tigercc.query.MessageBlackQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IMessageBlackService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/messageBlack")
public class MessageBlackController {
    
    @Resource
    public IMessageBlackService messageBlackService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody MessageBlack messageBlack) {
        if (messageBlack.getId() != null) {
            messageBlackService.updateById(messageBlack);
        } else {
            messageBlackService.insert(messageBlack);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        messageBlackService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(messageBlackService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(messageBlackService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody MessageBlackQuery query) {
        Page<MessageBlack> page = new Page<MessageBlack>(query.getPage(), query.getRows());
        page = messageBlackService.selectPage(page);
        return JsonResult.success(new PageList<MessageBlack>(page.getTotal(), page.getRecords()));
    }
    
    @RequestMapping(value = "/saveBlack", method = RequestMethod.POST)
    public JsonResult save(@RequestBody BlackDto dto) {
        if (dto.getId() != null) {
            messageBlackService.updateBlack(dto);
        } else {
            messageBlackService.saveBlack(dto);
        }
        return JsonResult.success();
    }
}
