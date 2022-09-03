package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.KillCourse;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.KillCourseMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IKillActivityService;
import org.yjhking.tigercc.service.IKillCourseService;
import org.yjhking.tigercc.utils.AssertUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-09-01
 */
@Service
public class KillCourseServiceImpl extends ServiceImpl<KillCourseMapper, KillCourse> implements IKillCourseService {
    @Resource
    private IKillActivityService killActivityService;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    
    @Override
    public void add(KillCourse killCourse) {
        Long activityId = killCourse.getActivityId();
        AssertUtils.isNull(selectByIdActiveIdAndCourseId(activityId, killCourse.getCourseId())
                , GlobalErrorCode.KILL_COURSE_REPEAT);
        // 查找秒杀活动并复制到秒杀课程里
        BeanUtils.copyProperties(killActivityService.selectById(activityId), killCourse);
        killCourse.setCreateTime(new Date());
        insert(killCourse);
    }
    
    @Override
    public JsonResult onlineAll() {
        BoundHashOperations<Object, Object, KillCourse> killCourse =
                redisTemplate.boundHashOps(RedisConstants.KILL_COURSES);
        return JsonResult.success(killCourse.values());
    }
    
    @Override
    public JsonResult onlineOne(Long killId, Long activityId) {
        AssertUtils.isNotNull(killId, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        AssertUtils.isNotNull(activityId, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        return JsonResult.success((KillCourse) redisTemplate.opsForHash()
                .get(RedisConstants.KILL_COURSES, killId.toString()));
    }
    
    private KillCourse selectByIdActiveIdAndCourseId(Long activityId, Long courseId) {
        return selectOne(new EntityWrapper<KillCourse>().eq(KillCourse.ACTIVITY_ID, activityId)
                .eq(KillCourse.COURSE_ID, courseId));
    }
}
