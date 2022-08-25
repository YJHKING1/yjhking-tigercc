package org.yjhking.tigercc.feignclient;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@Component
public class MediaFeignClientFallbackFactory implements FallbackFactory<MediaFeignClient> {
    @Override
    public MediaFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        return new MediaFeignClient() {
            @Override
            public JsonResult selectByCourseId(Long courseId) {
                return JsonResult.error();
            }
        };
    }
}
