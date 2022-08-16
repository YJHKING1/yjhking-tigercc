package org.yjhking.tigercc.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@FeignClient(value = "service-uaa", fallbackFactory = UaaFeignClientFallbackFactory.class)
public interface UaaFeignClient {
    @PostMapping("/login/insert")
    public JsonResult insert(@RequestBody RegisterDto dto);
}
