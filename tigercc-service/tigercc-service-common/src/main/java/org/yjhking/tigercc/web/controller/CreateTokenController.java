package org.yjhking.tigercc.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.CreateTokenService;

import javax.annotation.Resource;

/**
 * @author YJH
 */
@RestController
@RequestMapping("/createToken")
public class CreateTokenController {
    @Resource
    private CreateTokenService createTokenService;
    
    @GetMapping("/{ids}")
    public JsonResult createToken(@PathVariable String ids) {
        return createTokenService.createToken(ids);
    }
}
