package org.yjhking.tigercc.feignclient;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@Component
public class UaaFeignClientFallbackFactory implements FallbackFactory<UaaFeignClient> {
    @Override
    public UaaFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        return new UaaFeignClient() {
            @Override
            public JsonResult insert(RegisterDto dto) {
                return JsonResult.error(GlobalErrorCode.UAA_ERROR);
            }
        };
    }
}
