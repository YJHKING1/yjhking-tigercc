package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.IMessageEmailService;
import org.yjhking.tigercc.domain.MessageEmail;
import org.yjhking.tigercc.query.MessageEmailQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messageEmail")
public class MessageEmailController {

    @Autowired
    public IMessageEmailService messageEmailService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody MessageEmail messageEmail) {
        if (messageEmail.getId() != null){
                messageEmailService.updateById(messageEmail);
        }else{
                messageEmailService.insert(messageEmail);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            messageEmailService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(messageEmailService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(messageEmailService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody MessageEmailQuery query) {
        Page<MessageEmail> page = new Page<MessageEmail>(query.getPage(), query.getRows());
        page = messageEmailService.selectPage(page);
        return JsonResult.success(new PageList<MessageEmail>(page.getTotal(), page.getRecords()));
    }
}
