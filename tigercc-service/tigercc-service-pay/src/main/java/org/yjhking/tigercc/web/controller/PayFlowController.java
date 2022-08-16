package org.yjhking.tigercc.web.controller;

import org.yjhking.tigercc.service.IPayFlowService;
import org.yjhking.tigercc.domain.PayFlow;
import org.yjhking.tigercc.query.PayFlowQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/payFlow")
public class PayFlowController {

    @Resource
    public IPayFlowService payFlowService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody PayFlow payFlow) {
        if (payFlow.getId() != null){
                payFlowService.updateById(payFlow);
        }else{
                payFlowService.insert(payFlow);
        }
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
            payFlowService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(payFlowService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(payFlowService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody PayFlowQuery query) {
        Page<PayFlow> page = new Page<PayFlow>(query.getPage(), query.getRows());
        page = payFlowService.selectPage(page);
        return JsonResult.success(new PageList<PayFlow>(page.getTotal(), page.getRecords()));
    }
}
