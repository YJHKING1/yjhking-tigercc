package org.yjhking.tigercc.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@FeignClient(value = "service-search", fallbackFactory = SearchFeignClientFallbackFactory.class)
public interface SearchFeignClient {
    @PostMapping("/es/course/save")
    public JsonResult saveCourse(@RequestBody CourseDoc courseDoc);
    
    @PostMapping("/es/course/delete")
    public JsonResult deleteCourse(@RequestBody CourseDoc courseDoc);
}
