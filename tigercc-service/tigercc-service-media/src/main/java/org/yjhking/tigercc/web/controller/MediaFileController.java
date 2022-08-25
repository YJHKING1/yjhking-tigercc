package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yjhking.tigercc.domain.MediaFile;
import org.yjhking.tigercc.query.MediaFileQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IMediaFileService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mediaFile")
@Slf4j
public class MediaFileController {
    
    
    @Resource
    public IMediaFileService mediaFileService;
    
    //===============================================================
    //文件注册，检查文件是否已经上传
    @PostMapping("/register")
    public JsonResult register(@RequestParam("fileMd5") String fileMd5,     // 文件唯一标识
                               @RequestParam("fileName") String fileName,   // 文件名
                               @RequestParam("fileSize") Long fileSize,     // 文件大小
                               @RequestParam("mimetype") String mimetype,   // mime类型
                               @RequestParam("fileExt") String fileExt) {   //文件扩展名
        log.info("文件上传-文件注册,fileName={},fileMd5={}", fileName, fileMd5);
        return mediaFileService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
    }
    
    //校验文件块是否已经存在了
    @PostMapping("/checkchunk")
    public JsonResult checkchunk(
            // 文件唯一标识
            @RequestParam("fileMd5") String fileMd5,
            // 当前分块下标
            @RequestParam("chunk") Integer chunk,
            // 当前分块大小
            @RequestParam("chunkSize") Integer chunkSize) {
        log.info("文件上传-检查文件块是否存在；{}", fileMd5);
        return mediaFileService.checkchunk(fileMd5, chunk, chunkSize);
    }
    
    //上传分块后的文件
    @PostMapping("/uploadchunk")
    public JsonResult uploadchunk(
            //分块后的文件
            @RequestParam("file") MultipartFile file,
            // 文件唯一标识
            @RequestParam("fileMd5") String fileMd5,
            // 第几块，分块的索引
            @RequestParam("chunk") Integer chunk) {
        
        log.info("文件上传 fileName={},fileMd5={}", file.getOriginalFilename(), fileMd5);
        return mediaFileService.uploadchunk(file, fileMd5, chunk);
    }
    
    //分块都上传成功之后，合并分块
    @PostMapping("/mergechunks")
    public JsonResult mergechunks(
            // 课程章节ID
            @RequestParam("chapterId") Long chapterId,
            // 课程ID
            @RequestParam("courseId") Long courseId,
            // 课程序列号
            @RequestParam("videoNumber") Integer videoNumber,
            // 课程章节ID
            @RequestParam("name") String name,
            //章节名
            @RequestParam("chapterName") String chapterName,
            //课程名
            @RequestParam("courseName") String courseName,
            // 文件唯一标识
            @RequestParam("fileMd5") String fileMd5,
            // 源文件名
            @RequestParam("fileName") String fileName,
            // 文件总大小
            @RequestParam("fileSize") Long fileSize,
            // 文件的mimi类型
            @RequestParam("mimetype") String mimetype,
            // 文件扩展名
            @RequestParam("fileExt") String fileExt) {
        
        log.info("合并文件 fileName={} ，fileMd5={} ", fileName, fileMd5);
        return mediaFileService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt, chapterId, courseId, videoNumber, name, courseName, chapterName);
    }
    
    /**
     * 保存和修改公用的
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult saveOrUpdate(@RequestBody MediaFile mediaFile) {
        if (mediaFile.getId() != null) {
            mediaFileService.updateById(mediaFile);
        } else {
            mediaFileService.insert(mediaFile);
        }
        return JsonResult.success();
    }
    
    /**
     * 删除对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable("id") Long id) {
        mediaFileService.deleteById(id);
        return JsonResult.success();
    }
    
    /**
     * 获取对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResult get(@PathVariable("id") Long id) {
        return JsonResult.success(mediaFileService.selectById(id));
    }
    
    
    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        return JsonResult.success(mediaFileService.selectList(null));
    }
    
    
    /**
     * 带条件分页查询数据
     */
    @RequestMapping(value = "/pagelist", method = RequestMethod.POST)
    public JsonResult page(@RequestBody MediaFileQuery query) {
        Page<MediaFile> page = new Page<MediaFile>(query.getPage(), query.getRows());
        page = mediaFileService.selectPage(page);
        return JsonResult.success(new PageList<MediaFile>(page.getTotal(), page.getRecords()));
    }
    @GetMapping("/getMediaFile/{id}")
    public JsonResult selectByCourseId(@PathVariable("id") Long courseId){
        return mediaFileService.selectByCourseId(courseId);
    }
    @GetMapping("/getForUser/{id}")
    public JsonResult getUrlForUserById(@PathVariable("id") Long id){
        return mediaFileService.getUrlForUserById(id);
    }
}
