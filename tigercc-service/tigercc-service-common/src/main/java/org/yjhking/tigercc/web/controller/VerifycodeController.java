package org.yjhking.tigercc.web.controller;

import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.dto.MobileCodeDto;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IVerifycodeService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 验证码接口
 *
 * @author YJH
 */
@RestController
@RequestMapping("/verifycode")
public class VerifycodeController {
    @Resource
    private IVerifycodeService verifycodeService;
    
    /**
     * 获取短信验证码
     */
    @PostMapping("/sendSmsCode")
    public JsonResult sendSmsCode(@RequestBody @Valid MobileCodeDto mobileCodeDto) {
        return verifycodeService.sendSmsCode(mobileCodeDto);
    }
    
    /**
     * 获取图片验证码
     */
    @GetMapping("/imageCode/{key}")
    public JsonResult imageCode(@PathVariable("key") String key) {
        return verifycodeService.imageCode(key);
    }
}
