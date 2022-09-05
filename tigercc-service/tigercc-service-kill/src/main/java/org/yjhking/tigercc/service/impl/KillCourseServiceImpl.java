package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.KillCourse;
import org.yjhking.tigercc.dto.KillParamDto;
import org.yjhking.tigercc.dto.PreCourseOrder2RedisDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.KillCourseMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IKillActivityService;
import org.yjhking.tigercc.service.IKillCourseService;
import org.yjhking.tigercc.utils.AssertUtils;
import org.yjhking.tigercc.utils.CodeGenerateUtils;

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
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    
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
        return JsonResult.success((KillCourse) redisTemplate.opsForHash()
                .get(RedisConstants.KILL_COURSES, killId.toString()));
    }
    
    @Override
    public JsonResult kill(KillParamDto dto) {
        // todo 假数据
        long loginId = 3L;
        // 重复秒杀判断
        String killLog = RedisConstants.KILL_LOG + loginId + RedisConstants.REDIS_VERIFY + dto.getKillCourseId();
        AssertUtils.isFalse(redisTemplate.hasKey(killLog), GlobalErrorCode.KILL_REPEAT);
        KillCourse killCourse = (KillCourse) onlineOne(dto.getKillCourseId(), null).getData();
        AssertUtils.isNotNull(killCourse, GlobalErrorCode.KILL_ITEM_ERROR);
        AssertUtils.isTrue(killCourse.getKilling(), GlobalErrorCode.KILL_TIME_ERROR);
        AssertUtils.isEquals(killCourse.getCode(), dto.getCode(), GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        int killCount = NumberConstants.ONE;
        boolean killSuccess = redissonClient.getSemaphore(RedisConstants.STORE + killCourse.getId())
                .tryAcquire(killCount);
        AssertUtils.isTrue(killSuccess, GlobalErrorCode.KILL_IS_ERROR);
        String orderNo = CodeGenerateUtils.generateOrderSn(loginId);
        // 将订单储存到redis
        String orderKey = RedisConstants.ORDER + loginId + RedisConstants.REDIS_VERIFY + orderNo;
        redisTemplate.opsForValue().set(orderKey
                , new PreCourseOrder2RedisDto(killCourse.getKillPrice(), killCount, killCourse.getCourseId()
                        , killCourse.getCourseName(), killCourse.getCoursePic(), orderNo, killCourse.getId(), loginId));
        // 防止重复秒杀
        redisTemplate.opsForValue().set(killLog, "");
        // 秒杀超时处理
        rocketMQTemplate.syncSend(MQConstants.KILL_GROUP, MessageBuilder.withPayload(orderKey).build()
                , MQConstants.TIMEOUT_2000, MQConstants.DELAY_LEVEL_5);
        return JsonResult.success(orderNo);
    }
    
    @Override
    public void cancelPreOrder(String orderKey) {
        PreCourseOrder2RedisDto dto = (PreCourseOrder2RedisDto) redisTemplate.opsForValue().get(orderKey);
        AssertUtils.isNotNull(dto, GlobalErrorCode.KILL_ORDER_ERROR);
        // 退库
        redissonClient.getSemaphore(RedisConstants.STORE + dto.getKillCourseId()).release(dto.getCount());
        // 删除预创单
        redisTemplate.delete(orderKey);
    }
    
    private KillCourse selectByIdActiveIdAndCourseId(Long activityId, Long courseId) {
        return selectOne(new EntityWrapper<KillCourse>().eq(KillCourse.ACTIVITY_ID, activityId)
                .eq(KillCourse.COURSE_ID, courseId));
    }
}
