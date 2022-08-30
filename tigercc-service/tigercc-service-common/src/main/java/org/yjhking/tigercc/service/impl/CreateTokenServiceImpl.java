package org.yjhking.tigercc.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.CreateTokenService;
import org.yjhking.tigercc.utils.AssertUtils;
import org.yjhking.tigercc.utils.StrUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author YJH
 */
@Service
public class CreateTokenServiceImpl implements CreateTokenService {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    private static final Integer TOKEN_LENGTH = 6;
    private static final Integer TOKEN_REDIS_TIMEOUT = 10;
    
    @Override
    public JsonResult createToken(String ids) {
        AssertUtils.isHasLength(ids, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // todo 假数据 用户id
        Long loginId = 3L;
        String token = StrUtils.getComplexRandomString(TOKEN_LENGTH);
        redisTemplate.opsForValue().set(RedisConstants.TOKEN + loginId + RedisConstants.REDIS_VERIFY + ids
                , token, TOKEN_REDIS_TIMEOUT, TimeUnit.MINUTES);
        return JsonResult.success(token);
    }
}
