package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.PayOrder;
import org.yjhking.tigercc.query.PayOrderQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IPayOrderService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/payOrder")
public class PayOrderController {
    
    @Resource
    public IPayOrderService payOrderService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody PayOrder payOrder) {
        if (payOrder.getId() != null) {
            payOrderService.updateById(payOrder);
        } else {
            payOrderService.insert(payOrder);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        payOrderService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(payOrderService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(payOrderService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody PayOrderQuery query) {
        Page<PayOrder> page = new Page<PayOrder>(query.getPage(), query.getRows());
        page = payOrderService.selectPage(page);
        return JsonResult.success(new PageList<PayOrder>(page.getTotal(), page.getRecords()));
    }
    
    @GetMapping("/checkPayOrder/{orderNo}")
    public JsonResult checkPayOrder(@PathVariable("orderNo") String orderNo) {
        return JsonResult.success(null != payOrderService.selectByOrderNo(orderNo));
    }
}
