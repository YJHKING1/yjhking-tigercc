package org.yjhking.tigercc.service;

import org.yjhking.tigercc.dto.SearchDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
public interface CourseService {
    JsonResult search(SearchDto searchDto);
}
