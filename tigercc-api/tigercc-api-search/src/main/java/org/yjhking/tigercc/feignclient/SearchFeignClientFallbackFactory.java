package org.yjhking.tigercc.feignclient;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
@Component
public class SearchFeignClientFallbackFactory implements FallbackFactory<SearchFeignClient> {
    @Override
    public SearchFeignClient create(Throwable throwable) {
        throwable.printStackTrace();
        return new SearchFeignClient() {
            @Override
            public JsonResult saveCourse(CourseDoc courseDoc) {
                return JsonResult.error(GlobalErrorCode.SEARCH_ERROR);
            }
            
            @Override
            public JsonResult deleteCourse(CourseDoc courseDoc) {
                return JsonResult.error(GlobalErrorCode.SEARCH_ERROR);
            }
        };
    }
}
