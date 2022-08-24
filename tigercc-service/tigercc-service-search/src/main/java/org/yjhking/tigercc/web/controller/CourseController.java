package org.yjhking.tigercc.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yjhking.tigercc.dto.SearchDto;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.CourseService;

import javax.annotation.Resource;

/**
 * @author YJH
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    private CourseService courseService;
    
    @PostMapping("/search")
    public JsonResult search(@RequestBody SearchDto searchDto) {
        return courseService.search(searchDto);
    }
}
