package org.yjhking.tigercc.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.dto.PayParamDto;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.PayService;

import javax.annotation.Resource;

/**
 * @author YJH
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Resource
    private PayService payService;
    
    @PostMapping("/apply")
    public JsonResult apply(@RequestBody PayParamDto dto) {
        return payService.apply(dto);
    }
    
    @PostMapping("/alipay/notify")
    public String notify(@RequestBody AlipayNotifyDto dto) {
        try {
            return payService.alipayNotify(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return TigerccConstants.ALIPAY_FAIL;
        }
    }
}
