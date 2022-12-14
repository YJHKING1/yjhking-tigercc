package org.yjhking.tigercc.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.doc.CourseDoc;
import org.yjhking.tigercc.domain.*;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.dto.CourseStatus;
import org.yjhking.tigercc.dto.MessageProperties;
import org.yjhking.tigercc.dto.StationMessage2MQDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.feignclient.MediaFeignClient;
import org.yjhking.tigercc.feignclient.SearchFeignClient;
import org.yjhking.tigercc.mapper.CourseMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.*;
import org.yjhking.tigercc.utils.AssertUtils;
import org.yjhking.tigercc.utils.StrUtils;
import org.yjhking.tigercc.utils.VerifyUtils;
import org.yjhking.tigercc.vo.CourseDataForDetailVO;
import org.yjhking.tigercc.vo.CourseDataOrderVo;
import org.yjhking.tigercc.vo.CourseItemDataOrderVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    private ICourseDetailService courseDetailService;
    @Resource
    private ICourseMarketService courseMarketService;
    @Resource
    private ICourseResourceService courseResourceService;
    @Resource
    private ICourseSummaryService courseSummaryService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private ICourseTeacherService courseTeacherService;
    @Resource
    private SearchFeignClient searchFeignClient;
    @Resource
    private RocketMQTemplate template;
    @Resource
    private MessageProperties messageProperties;
    @Resource
    private ICourseUserLearnService courseUserLearnService;
    @Resource
    private ICourseChapterService chapterService;
    @Resource
    private MediaFeignClient mediaFeignClient;
    @Resource
    private ICourseRecommendService courseRecommendService;
    
    @Transactional
    @Override
    public void save(CourseDto dto) {
        // 校验数据
        verify(dto);
        // 保存教师
        saveTeacher(dto);
        // 保存课程
        AssertUtils.isTrue(saveCourse(dto), GlobalErrorCode.COURSE_ERROR);
        // 保存课程详情
        AssertUtils.isTrue(courseDetailService.save(dto), GlobalErrorCode.COURSE_ERROR);
        // 保存课程营销
        AssertUtils.isTrue(courseMarketService.save(dto), GlobalErrorCode.COURSE_ERROR);
        // 保存课程资源
        AssertUtils.isTrue(courseResourceService.save(dto), GlobalErrorCode.COURSE_ERROR);
        // 初始化课程统计
        AssertUtils.isTrue(courseSummaryService.save(dto.getCourse().getId()), GlobalErrorCode.COURSE_ERROR);
        // 保存教师中间表
        saveCourseTeacher(dto);
    }
    
    @Override
    public JsonResult onLineCourse(Long id) {
        AssertUtils.isNotNull(id, GlobalErrorCode.SERVICE_OBJECT_IS_NULL);
        CourseDoc courseDoc = id2CourseDoc(id, NumberConstants.ONE);
        searchFeignClient.saveCourse(courseDoc);
        // 用MQ发送站内消息
        sendMQMessage(courseDoc);
        // todo 发送短信
        // todo 发送邮件
        return JsonResult.success();
    }
    
    /**
     * 发送站内消息
     */
    private void sendMQMessage(CourseDoc courseDoc) {
        template.syncSend(MQConstants.TOPIC_COURSE_FILE + RedisConstants.REDIS_VERIFY +
                MQConstants.TAGS_COURSE_FILE, MessageBuilder.withPayload(JSON.toJSONString(
                new StationMessage2MQDto(messageProperties.getTitle(), String.format(
                        messageProperties.getContent(), courseDoc.getName(), courseDoc.getId())
                        , messageProperties.getType(), Collections.singletonList(courseDoc.getId())))).build());
    }
    
    /**
     * id转课程Doc对象
     */
    private CourseDoc id2CourseDoc(Long id, int status) {
        Course course = selectById(id);
        course.setStatus(status);
        course.setOnlineTime(new Date());
        // 更新上下架状态
        updateById(course);
        CourseDoc courseDoc = new CourseDoc();
        BeanUtils.copyProperties(course, courseDoc);
        CourseMarket courseMarket = courseMarketService.selectById(id);
        BeanUtils.copyProperties(courseMarket, courseDoc);
        BeanUtils.copyProperties(courseSummaryService.selectById(id), courseDoc);
        if (VerifyUtils.equals(courseMarket.getCharge(), CourseMarket.CHARGE_FREE))
            courseDoc.setChargeName(CourseMarket.CHARGE_FREE_NAME);
        else if (VerifyUtils.equals(courseMarket.getCharge(), CourseMarket.CHARGE_UN_FREE))
            courseDoc.setChargeName(CourseMarket.CHARGE_UN_FREE_NAME);
        return courseDoc;
    }
    
    @Override
    public JsonResult offLineCourse(Long id) {
        AssertUtils.isNotNull(id, GlobalErrorCode.SERVICE_OBJECT_IS_NULL);
        searchFeignClient.deleteCourse(id2CourseDoc(id, NumberConstants.ZERO));
        // 取消课程推荐
        courseRecommendService.saveOff(id);
        return JsonResult.success();
    }
    
    @Override
    public JsonResult selectCourseDataForDetail(Long id) {
        // 判断id
        AssertUtils.isNotNull(id, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 查询课程
        Course course = selectById(id);
        AssertUtils.isNotNull(course, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 判断课程是否上线
        AssertUtils.isEquals(course.getStatus(), Course.STATUS_ONLINE
                , GlobalErrorCode.COURSE_IS_NOT_ONLINE);
        // 查询并返回
        return JsonResult.success(new CourseDataForDetailVO(course, courseMarketService.selectById(id)
                , courseDetailService.selectById(id), courseSummaryService.selectById(id)
                , selectTeachersByCourseId(id), selectChaptersByCourseId(id)));
    }
    
    @Override
    public JsonResult selectCourseStatusForUser(Long courseId) {
        // 判断id是否为空
        AssertUtils.isNotNull(courseId, GlobalErrorCode.COURSE_ID_IS_NULL);
        // 查询课程
        Course course = selectById(courseId);
        // 判断课程是否为空
        AssertUtils.isNotNull(course, GlobalErrorCode.COURSE_IS_NULL);
        // 查询课程营销
        CourseMarket courseMarket = courseMarketService.selectById(courseId);
        AssertUtils.isNotNull(courseMarket, GlobalErrorCode.COURSE_IS_NULL);
        // 登录对象
        // todo 假数据
        Long loginId = 3L;
        // 是否上线
        AssertUtils.isEquals(course.getStatus(), Course.STATUS_ONLINE, GlobalErrorCode.COURSE_IS_NOT_ONLINE);
        // 判断是否免费
        if (VerifyUtils.equals(courseMarket.getCharge(), CourseMarket.CHARGE_FREE))
            return JsonResult.success(new CourseStatus());
        // 是否购买
        AssertUtils.isNotNull(selectByUserIdAndCourseId(loginId, courseId), GlobalErrorCode.COURSE_IS_NOT_BUY);
        return JsonResult.success(new CourseStatus());
    }
    
    @Override
    public JsonResult selectCourseDataForOrder(String courseIds) {
        AssertUtils.isHasLength(courseIds, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 分割id并查询课程
        List<Long> courseIdsList = StrUtils.splitStr2LongArr(courseIds, TigerccConstants.SEPARATOR);
        List<Course> courses = selectBatchIds(courseIdsList);
        AssertUtils.isNotNull(courses, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        AssertUtils.isEquals(courses.size(), courseIdsList.size(), GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 批量查询营销
        Map<Long, CourseMarket> courseMarketsMap = courseMarketService.selectBatchIds(courseIdsList).stream()
                .collect(Collectors.toMap(CourseMarket::getId, CourseMarket -> CourseMarket));
        // 课程详情列表
        List<CourseItemDataOrderVo> itemVos = new ArrayList<>(courseIdsList.size());
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Course course : courses) {
            CourseMarket courseMarket = courseMarketsMap.get(course.getId());
            AssertUtils.isEquals(course.getStatus(), Course.STATUS_ONLINE, GlobalErrorCode.COURSE_IS_NOT_ONLINE);
            AssertUtils.isEquals(courseMarket.getCharge(), CourseMarket.CHARGE_UN_FREE
                    , GlobalErrorCode.COURSE_IS_FREE);
            itemVos.add(new CourseItemDataOrderVo(course, courseMarket));
            totalAmount = totalAmount.add(courseMarket.getPrice());
        }
        return JsonResult.success(new CourseDataOrderVo(itemVos, totalAmount));
    }
    
    @Override
    public JsonResult recommendOn(Long id) {
        AssertUtils.isNotNull(id, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        courseRecommendService.saveOn(id);
        return JsonResult.success();
    }
    
    @Override
    public JsonResult recommendOff(Long id) {
        AssertUtils.isNotNull(id, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        courseRecommendService.saveOff(id);
        return JsonResult.success();
    }
    
    /**
     * 查询用户有没有购买某个课程
     */
    private CourseUserLearn selectByUserIdAndCourseId(Long loginId, Long courseId) {
        return courseUserLearnService.selectOne(new EntityWrapper<CourseUserLearn>().eq(TigerccConstants.LOGIN_ID
                , loginId).eq(TigerccConstants.COURSE_ID, courseId).eq(TigerccConstants.STATE, NumberConstants.ZERO));
    }
    
    /**
     * 根据课程查询章节
     */
    private List<CourseChapter> selectChaptersByCourseId(Long courseId) {
        List<CourseChapter> courseChapters = chapterService.selectList(
                new EntityWrapper<CourseChapter>().eq(TigerccConstants.COURSE_ID, courseId));
        JsonResult jsonResult = mediaFeignClient.selectByCourseId(courseId);
        AssertUtils.isTrue(jsonResult.isSuccess(), GlobalErrorCode.MEDIA_ERROR);
        AssertUtils.isNotNull(jsonResult.getData(), GlobalErrorCode.MEDIA_LIST_NULL);
        JSON.parseArray(JSON.toJSONString(jsonResult.getData()), MediaFile.class).forEach(mediaFile -> {
            mediaFile.setFileUrl("");
            CourseChapter courseChapter = courseChapters.stream().collect(Collectors.toMap(CourseChapter::getId
                    , CourseChapter -> CourseChapter)).get(mediaFile.getChapterId());
            if (VerifyUtils.nonEmpty(courseChapter)) courseChapter.getMediaFileList().add(mediaFile);
        });
        return courseChapters;
    }
    
    /**
     * 根据课程查询老师
     */
    private List<Teacher> selectTeachersByCourseId(Long courseId) {
        return teacherService.selectBatchIds(courseTeacherService.selectList(new EntityWrapper<CourseTeacher>()
                        .eq("course_id", courseId)).stream().map(CourseTeacher::getTeacherId)
                .collect(Collectors.toList()));
    }
    
    /**
     * 保存教师
     *
     * @param dto 课程数据
     */
    private void saveTeacher(CourseDto dto) {
        dto.getCourse().setTeacherNames(teacherService.selectBatchIds(dto.getTeacharIds()).stream()
                .map(Teacher::getName).collect(Collectors.joining(TigerccConstants.SAVE_TEACHER_SEPARATOR)));
    }
    
    /**
     * 保存课程
     *
     * @param dto 课程数据
     */
    private void saveCourseTeacher(CourseDto dto) {
        dto.getTeacharIds().forEach(teacherId -> {
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setCourseId(dto.getCourse().getId());
            courseTeacher.setTeacherId(teacherId);
            courseTeacherService.insert(courseTeacher);
        });
    }
    
    /**
     * 保存课程
     *
     * @param dto 课程数据
     * @return 是否保存成功
     */
    private boolean saveCourse(CourseDto dto) {
        Course course = dto.getCourse();
        course.setStatus(NumberConstants.ZERO);
        course.setChapterCount(NumberConstants.ZERO);
        return insert(course);
    }
    
    /**
     * 校验数据
     *
     * @param dto 课程数据
     */
    private void verify(CourseDto dto) {
        // 课程名重复校验
        AssertUtils.isNotHasLength(selectList(new EntityWrapper<Course>()
                .eq(RedisConstants.NAME, dto.getCourse().getName())), GlobalErrorCode.COURSE_NAME_REPEAT);
        // 校验课程时间
        AssertUtils.isTimeBefore(dto.getCourse().getStartTime(), dto.getCourse().getEndTime()
                , GlobalErrorCode.COURSE_TIME_ERROR);
    }
}
