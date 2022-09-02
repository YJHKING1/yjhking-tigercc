package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.KillActivity;
import org.yjhking.tigercc.domain.KillCourse;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.KillActivityMapper;
import org.yjhking.tigercc.service.IKillActivityService;
import org.yjhking.tigercc.service.IKillCourseService;
import org.yjhking.tigercc.utils.AssertUtils;
import org.yjhking.tigercc.utils.StrUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-09-01
 */
@Service
public class KillActivityServiceImpl extends ServiceImpl<KillActivityMapper, KillActivity> implements IKillActivityService {
    @Resource
    private IKillCourseService killCourseService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 秒杀码长度
     */
    private static final int CODE_LENGTH = 6;
    
    @Override
    @Transactional
    public void publish(Long id) {
        KillActivity killActivity = selectById(id);
        AssertUtils.isNotNull(killActivity, GlobalErrorCode.KILL_ACTIVITY_NOT_EXIST);
        AssertUtils.isEquals(killActivity.getPublishStatus(), KillActivity.STATUS_WAIT_PUBLISH, GlobalErrorCode.KILL_EVENT_PUBLISHED);
        List<KillCourse> killCourses = killCourseService.selectList(new EntityWrapper<KillCourse>().eq(KillCourse.ACTIVITY_ID, id));
        AssertUtils.isHasLength(killCourses, GlobalErrorCode.KILL_NO_ITEMS);
        killActivity.setPublishStatus(KillActivity.STATUS_SUCCESS_PUBLISH);
        killActivity.setPublishTime(new Date());
        updateById(killActivity);
        killCourses.forEach(killCourse -> {
            Integer killCount = killCourse.getKillCount();
            if (redissonClient.getSemaphore(RedisConstants.STORE + killCourse.getId()).trySetPermits(killCount)) {
                killCourse.setCode(StrUtils.getComplexRandomString(CODE_LENGTH));
                killCourse.setPublishStatus(killActivity.getPublishStatus());
                // 把商品存到redis，map key为商品id，value为商品
                redisTemplate.opsForHash().put(RedisConstants.KILL_COURSES, killCourse.getId().toString(), killCourse);
                killCourse.setPublishTime(killActivity.getPublishTime());
                killCourseService.updateById(killCourse);
            }
        });
    }
}
