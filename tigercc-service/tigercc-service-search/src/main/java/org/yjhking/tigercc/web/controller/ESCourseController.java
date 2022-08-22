package org.yjhking.tigercc.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ESCourseService;

import javax.annotation.Resource;

/**
 * @author YJH
 */
@RestController
@RequestMapping("/es/course")
public class ESCourseController {
    @Resource
    private ESCourseService esCourseService;
    
    @PostMapping("/save")
    public JsonResult saveCourse(@RequestBody CourseDoc courseDoc) {
        return esCourseService.save(courseDoc);
    }
    
    @PostMapping("/delete")
    public JsonResult deleteCourse(@RequestBody CourseDoc courseDoc) {
        return esCourseService.delete(courseDoc);
    }
}
