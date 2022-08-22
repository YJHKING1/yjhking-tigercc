package org.yjhking.tigercc.service.impl;

import org.springframework.stereotype.Service;
import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.repository.CourseESRepository;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ESCourseService;

import javax.annotation.Resource;

/**
 * @author YJH
 */
@Service
public class ESCourseServiceImpl implements ESCourseService {
    @Resource
    private CourseESRepository courseESRepository;
    
    @Override
    public JsonResult save(CourseDoc courseDoc) {
        courseESRepository.save(courseDoc);
        return JsonResult.success();
    }
    
    @Override
    public JsonResult delete(CourseDoc courseDoc) {
        courseESRepository.delete(courseDoc);
        return JsonResult.success();
    }
}
