package org.yjhking.tigercc.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@FeignClient(value = "service-media", fallbackFactory = MediaFeignClientFallbackFactory.class)
public interface MediaFeignClient {
    @GetMapping("/mediaFile/getMediaFile/{id}")
    public JsonResult selectByCourseId(@PathVariable("id") Long courseId);
}
