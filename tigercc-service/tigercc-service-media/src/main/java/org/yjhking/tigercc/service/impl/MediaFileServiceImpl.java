package org.yjhking.tigercc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.MediaFile;
import org.yjhking.tigercc.dto.CourseStatus;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
import org.yjhking.tigercc.feignclient.CourseFeignClient;
import org.yjhking.tigercc.mapper.MediaFileMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IMediaFileService;
import org.yjhking.tigercc.utils.HlsVideoUtil;
import org.yjhking.tigercc.utils.Mp4VideoUtil;
import org.yjhking.tigercc.utils.VerificationUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
@Slf4j
public class MediaFileServiceImpl extends ServiceImpl<MediaFileMapper, MediaFile> implements IMediaFileService {
    
    @Resource
    private MediaFileMapper mediaFileMapper;
    @Resource
    private RocketMQTemplate template;
    //@Autowired
    //private MediaProducer mediaProducer;
    @Resource
    private CourseFeignClient courseFeignClient;
    /**
     * 配置
     * ===============================================================================================================
     */
    //上传文件根目录
    @Value("${media.upload-base-dir}")
    private String uploadPath;
    
    //推流服务器地址
    @Value("${media.rtmp}")
    private String srsRtmpPath;
    
    //推流服务器播放
    @Value("${media.play}")
    private String srsPalyPath;
    
    //ffmpeg绝对路径
    @Value("${media.ffmpeg‐path}")
    String ffmpeg_path;
    
    /**
     * 合并分片
     */
    @Override
    public JsonResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt,
                                  Long courseChapterId, Long courseId, Integer number, String name, String courseName, String chapterName) {
        Long startTime = System.currentTimeMillis();
        
        //获取块文件的路径
        String chunkfileFolderPath = getChunkFileFolderPath(fileMd5);
        
        //创建文件目录
        File chunkfileFolder = new File(chunkfileFolderPath);
        
        //目录是否存在， 不存在就创建目录
        if (!chunkfileFolder.exists()) {
            chunkfileFolder.mkdirs();
        }
        
        //合并文件，创建新的文件对象
        File mergeFile = new File(getFilePath(fileMd5, fileExt));
        
        // 合并文件存在先删除再创建
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        
        boolean newFile = false;
        
        try {
            //创建文件
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (!newFile) {
            //创建失败
            return JsonResult.error("创建文件失败！");
        }
        
        //获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles = getChunkFiles(chunkfileFolder);
        //合并文件
        mergeFile = mergeFile(mergeFile, chunkFiles);
        if (mergeFile == null) {
            return JsonResult.error("合并文件失败！");
        }
        //校验文件
        boolean checkResult = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkResult) {
            return JsonResult.error("文件校验失败！");
        }
        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        //MD5作为文件唯一ID
        mediaFile.setFileId(fileMd5);
        //文件名
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        //源文件名
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5, fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        mediaFile.setChapterId(courseChapterId);
        mediaFile.setCourseId(courseId);
        mediaFile.setName(name);
        mediaFile.setCourseName(courseName);
        mediaFile.setChapterName(chapterName);
        mediaFile.setFileUrl(srsPalyPath + mediaFile.getFileId() + ".m3u8");
        //状态为上传成功
        mediaFile.setFileStatus(1);
        
        //根据最新的number生成number
        Wrapper<MediaFile> wrapper = new EntityWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("chapter_id", courseChapterId);
        //某个课程下的某个章节下的视频数量
        Integer videoCount = mediaFileMapper.selectCount(wrapper);
        mediaFile.setNumber(videoCount + 1);
        
        mediaFileMapper.insert(mediaFile);
        String mediaFileString = JSON.toJSONString(mediaFile);
        Message<String> message = MessageBuilder.withPayload(mediaFileString).build();
        SendResult sendResult = template.syncSend(
                MQConstants.TOPIC_MEDIA_FILE + RedisConstants.REDIS_VERIFY + MQConstants.TAGS_MEDIA_FILE
                , message);
        // 文件上传到视频服务器做 断点续播
        // handleFile2m3u8(mediaFile);
        boolean success = sendResult.getSendStatus() == SendStatus.SEND_OK;
        log.info("合并文件耗时 {}", System.currentTimeMillis() - startTime);
        return success ? JsonResult.success() : JsonResult.error();
    }
    
    /**
     * 文件推流
     **/
    public JsonResult handleFile2m3u8(MediaFile mediaFile) {
        String fileType = mediaFile.getFileType();
        if (fileType == null) {
            return JsonResult.error("无效的扩展名");
        }
        
        //组装MP4文件名
        String mp4_name = mediaFile.getFileId() + ".mp4";
        
        //如果视频不是MP4需要进行格式转换
        if (!fileType.equals("mp4")) {
            //生成mp4的文件路径
            String video_path = uploadPath + mediaFile.getFilePath() + mediaFile.getFileName();
            //文件目录
            String mp4folder_path = uploadPath + mediaFile.getFilePath();
            //视频编码，生成mp4文件
            Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
            //生成MP4文件
            String result = videoUtil.generateMp4();
            if (result == null || !result.equals("success")) {
                //操作失败写入处理日志
                mediaFile.setFileStatus(3);
                mediaFileMapper.updateById(mediaFile);
                return JsonResult.error("视频转换mp4失败");
            }
        }
        
        mediaFile.setFileStatus(1);
        //处理状态为未处理
        mediaFileMapper.updateById(mediaFile);
        
        //此地址为mp4的本地地址
        String video_path = uploadPath + mediaFile.getFilePath() + mp4_name;
        
        //初始化推流工具
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path);
        hlsVideoUtil.init(srsRtmpPath, video_path, mediaFile.getFileId());
        //推流到云端
        String result = hlsVideoUtil.generateM3u8ToSrs();
        
        if (result == null || !result.equals("success")) {
            //操作失败写入处理日志
            mediaFile.setFileStatus(3);
            //处理状态为处理失败
            //MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            //mediaFileProcess_m3u8.setErrorMsg(result);
            //mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileMapper.updateById(mediaFile);
            return JsonResult.error("推流失败");
        }
        //获取m3u8列表
        //更新处理状态为成功
        mediaFile.setFileStatus(2);
        //m3u8文件url,播放使用
        //mediaFile.setFileUrl(srsPalyPath+mediaFile.getFileId()+".m3u8");
        mediaFileMapper.updateById(mediaFile);
        log.info("视频推流完成...");
        
        return JsonResult.success();
    }
    
    @Override
    public JsonResult selectByCourseId(Long courseId) {
        return JsonResult.success(selectList(new EntityWrapper<MediaFile>().eq(TigerccConstants.COURSE_ID, courseId)));
    }
    
    @Override
    public JsonResult getUrlForUserById(Long mediaId) {
        // 判断参数
        VerificationUtils.isNotNull(mediaId, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        MediaFile mediaFile = selectById(mediaId);
        VerificationUtils.isNotNull(mediaFile, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 获取课程状态：是否免费，是否购买，是否上线（Feign）
        JsonResult jsonResult = courseFeignClient.selectCourseStatusForUser(mediaFile.getCourseId());
        VerificationUtils.isTrue(jsonResult.isSuccess(), GlobalErrorCode.COURSE_ERROR);
        VerificationUtils.isNotNull(jsonResult.getData(), GlobalErrorCode.COURSE_ERROR);
        // 去课程服务编写查询接口，查询这三个状态
        CourseStatus courseStatus = JSON.parseObject(JSON.toJSONString(jsonResult.getData()), CourseStatus.class);
        // 判断课程是否上线
        VerificationUtils.isTrue(courseStatus.getOnlined(), GlobalErrorCode.COURSE_IS_NOT_ONLINE);
        // 免费直接返回播放地址
        if (courseStatus.getFree()) return JsonResult.success(mediaFile.getFileUrl());
        // 用户购买了返回播放地址
        if (courseStatus.getBuyed()) return JsonResult.success(mediaFile.getFileUrl());
        // 如果是试看：直接返回播放地址
        if (VerificationUtils.isValid(mediaFile.getFree()) && mediaFile.getFree())
            return JsonResult.success(mediaFile.getFileUrl());
        // 否则，不返回播放地址
        throw new GlobalCustomException(GlobalErrorCode.COURSE_IS_NOT_BUY);
    }
    
    /*
     *根据文件md5得到文件路径
     */
    private String getFilePath(String fileMd5, String fileExt) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
        return filePath;
    }
    
    //得到文件目录相对路径，路径中去掉根目录
    private String getFileFolderRelativePath(String fileMd5, String fileExt) {
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return filePath;
    }
    
    //得到文件所在目录
    private String getFileFolderPath(String fileMd5) {
        String fileFolderPath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return fileFolderPath;
    }
    
    //创建文件目录
    private boolean createFileFold(String fileMd5) {
        //创建上传文件目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            //创建文件夹
            boolean mkdirs = fileFolder.mkdirs();
            log.info("创建文件目录 {} ,结果 {}", fileFolder.getPath(), mkdirs);
            return mkdirs;
        }
        return true;
    }
    
    /**
     * 上传文件注册
     */
    @Override
    public JsonResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //检查文件是否上传
        // 1、得到文件的路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        
        //2、查询数据库文件是否存在
        MediaFile media = mediaFileMapper.selectById(
                selectOne(new EntityWrapper<MediaFile>().eq("file_id", fileMd5)));
        //文件存在直接返回
        if (file.exists() && media != null) {
            log.info("文件注册 {} ,文件已经存在", fileName);
            return JsonResult.error("上传文件已存在");
        }
        boolean fileFold = createFileFold(fileMd5);
        if (!fileFold) {
            //上传文件目录创建失败
            log.info("上传文件目录创建失败 {} ,文件已经存在", fileName);
            return JsonResult.error("上传文件目录失败");
        }
        return JsonResult.success();
    }
    
    //得到块文件所在目录
    private String getChunkFileFolderPath(String fileMd5) {
        String fileChunkFolderPath = getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
    }
    
    /**
     * 检查分片
     */
    @Override
    public JsonResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //获取块文件文件夹路径
        String chunkfileFolderPath = getChunkFileFolderPath(fileMd5);
        //块文件的文件名称以1,2,3..序号命名，没有扩展名
        File chunkFile = new File(chunkfileFolderPath + chunk);
        if (!chunkFile.exists()) {
            return JsonResult.error();
        }
        return JsonResult.success();
    }
    
    /**
     * 创建块文件目录
     */
    private boolean createChunkFileFolder(String fileMd5) { //创建上传文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            //创建文件夹
            boolean mkdirs = chunkFileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }
    
    /**
     * 上传分片
     */
    @Override
    public JsonResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        if (file == null) {
            return JsonResult.error("上传文件不能为null");
        }
        //创建块文件目录
        boolean fileFold = createChunkFileFolder(fileMd5);
        
        //块文件存放完整路径
        File chunkfile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        
        //上传的块文件
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkfile);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("文件上传失败！");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JsonResult.success();
    }
    
    
    //校验文件的md5值
    private boolean checkFileMd5(File mergeFile, String md5) {
        if (mergeFile == null || StringUtils.isEmpty(md5)) {
            return false;
        }
        //进行md5校验
        FileInputStream mergeFileInputstream = null;
        try {
            mergeFileInputstream = new FileInputStream(mergeFile);
            //得到文件的md5
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileInputstream);
            //比较md5
            if (md5.equalsIgnoreCase(mergeFileMd5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            try {
                mergeFileInputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    //获取所有块文件
    private List<File> getChunkFiles(File chunkfileFolder) {
        //获取路径下的所有块文件
        File[] chunkFiles = chunkfileFolder.listFiles();
        //将文件数组转成list，并排序
        List<File> chunkFileList = new ArrayList<File>();
        chunkFileList.addAll(Arrays.asList(chunkFiles));
        //排序
        Collections.sort(chunkFileList, (o1, o2) -> {
            if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                return 1;
            }
            return -1;
        });
        return chunkFileList;
    }
    
    //合并文件
    private File mergeFile(File mergeFile, List<File> chunkFiles) {
        try {
            //创建写文件对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            //遍历分块文件开始合并
            // 读取文件缓冲区
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFiles) {
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                //读取分块文件
                while ((len = raf_read.read(b)) != -1) {
                    //向合并文件中写数据
                    raf_write.write(b, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mergeFile;
    }
}
