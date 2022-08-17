package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.dto.BlackDto;
import org.yjhking.tigercc.query.MessageSmsQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IMessageSmsService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/messageSms")
public class MessageSmsController {
    
    @Resource
    public IMessageSmsService messageSmsService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody MessageSms messageSms) {
        if (messageSms.getId() != null) {
            messageSmsService.updateById(messageSms);
        } else {
            messageSmsService.insert(messageSms);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        messageSmsService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(messageSmsService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(messageSmsService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody MessageSmsQuery query) {
        Page<MessageSms> page = new Page<MessageSms>(query.getPage(), query.getRows());
        page = messageSmsService.selectPage(page);
        return JsonResult.success(new PageList<MessageSms>(page.getTotal(), page.getRecords()));
    }
    
    @PostMapping("/black")
    public JsonResult black(@RequestBody BlackDto dto) {
        return messageSmsService.black(dto);
    }
}
