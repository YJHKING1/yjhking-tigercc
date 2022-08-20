package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.yjhking.tigercc.domain.MediaFile;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface IMediaFileService extends IService<MediaFile> {
    
    /**
     * 文件上传之前的注册功能
     */
    JsonResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);
    
    /**
     * 校验文件块是否已经存在了
     */
    JsonResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);
    
    /**
     * 上传文件块
     */
    JsonResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk);
    
    /**
     * 合并文件快
     */
    JsonResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt,
                           Long courseChapterId, Long courseId, Integer number, String name, String courseName, String chapterName);
    
    //处理文件
    JsonResult handleFile2m3u8(MediaFile mediaFile);
}
