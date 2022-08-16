package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.UserAddress;
import org.yjhking.tigercc.query.UserAddressQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IUserAddressService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/userAddress")
public class UserAddressController {
    
    @Resource
    public IUserAddressService userAddressService;
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody UserAddress userAddress) {
        if (userAddress.getId() != null) {
            userAddressService.updateById(userAddress);
        } else {
            userAddressService.insert(userAddress);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        userAddressService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(userAddressService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(userAddressService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody UserAddressQuery query) {
        Page<UserAddress> page = new Page<UserAddress>(query.getPage(), query.getRows());
        page = userAddressService.selectPage(page);
        return JsonResult.success(new PageList<UserAddress>(page.getTotal(), page.getRecords()));
    }
}
