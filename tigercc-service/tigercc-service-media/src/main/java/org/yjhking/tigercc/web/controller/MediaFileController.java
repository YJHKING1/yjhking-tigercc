package org.yjhking.tigercc.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.*;
import org.yjhking.tigercc.domain.MediaFile;
import org.yjhking.tigercc.query.MediaFileQuery;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.result.PageList;
import org.yjhking.tigercc.service.IMediaFileService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mediaFile")
public class MediaFileController {
    
    @Resource
    public IMediaFileService mediaFileService;
    
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
}
