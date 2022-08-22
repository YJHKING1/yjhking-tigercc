package org.yjhking.tigercc.service;

import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.result.JsonResult;

/**
 * @author YJH
 */
public interface ESCourseService {
    JsonResult save(CourseDoc courseDoc);
    
    JsonResult delete(CourseDoc courseDoc);
}
