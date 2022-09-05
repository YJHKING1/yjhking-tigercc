package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.CourseOrder;
import org.yjhking.tigercc.dto.CourseKillOrderParamDto;
import org.yjhking.tigercc.dto.PlaceOrderDto;
import org.yjhking.tigercc.query.CourseOrderQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.ICourseOrderService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/courseOrder")
public class CourseOrderController {
    
    @Resource
    public ICourseOrderService courseOrderService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody CourseOrder courseOrder) {
        if (courseOrder.getId() != null) {
            courseOrderService.updateById(courseOrder);
        } else {
            courseOrderService.insert(courseOrder);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        courseOrderService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(courseOrderService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(courseOrderService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody CourseOrderQuery query) {
        Page<CourseOrder> page = new Page<CourseOrder>(query.getPage(), query.getRows());
        page = courseOrderService.selectPage(page);
        return JsonResult.success(new PageList<CourseOrder>(page.getTotal(), page.getRecords()));
    }
    
    @PostMapping("/placeOrder")
    public JsonResult placeOrder(@RequestBody PlaceOrderDto dto) {
        return JsonResult.success(courseOrderService.placeOrder(dto));
    }
    
    @PostMapping("/killPlaceOrder")
    public JsonResult killPlaceOrder(@RequestBody CourseKillOrderParamDto dto) {
        return courseOrderService.killPlaceOrder(dto);
    }
}
