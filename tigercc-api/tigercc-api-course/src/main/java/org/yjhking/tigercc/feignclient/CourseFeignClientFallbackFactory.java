package org.yjhking.tigercc.feignclient;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@Component
public class CourseFeignClientFallbackFactory implements FallbackFactory<CourseFeignClient> {
    @Override
    public CourseFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        return new CourseFeignClient() {
            @Override
            public JsonResult selectCourseStatusForUser(Long id) {
                return JsonResult.error();
            }
        };
    }
}
