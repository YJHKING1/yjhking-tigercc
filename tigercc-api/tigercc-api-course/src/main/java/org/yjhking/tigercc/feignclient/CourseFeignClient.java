package org.yjhking.tigercc.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@FeignClient(value = "service-course", fallbackFactory = CourseFeignClientFallbackFactory.class)
public interface CourseFeignClient {
    @GetMapping("/course/selectCourse/{id}")
    public JsonResult selectCourseStatusForUser(@PathVariable("id") Long id);
    @GetMapping("/course/info/{courseIds}")
    public JsonResult selectCourseDataForOrder(@PathVariable("courseIds") String courseIds);
}
