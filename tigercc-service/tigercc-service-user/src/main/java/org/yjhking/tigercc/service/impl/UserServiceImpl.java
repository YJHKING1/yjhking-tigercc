package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.feignclient.UaaFeignClient;
import org.yjhking.tigercc.mapper.UserMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IUserAccountService;
import org.yjhking.tigercc.service.IUserBaseInfoService;
import org.yjhking.tigercc.service.IUserService;
import org.yjhking.tigercc.utils.BitStatesUtils;
import org.yjhking.tigercc.utils.ObjectMapperUtils;
import org.yjhking.tigercc.utils.RedisUtils;
import org.yjhking.tigercc.utils.VerificationUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /**
     * redis
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private IUserBaseInfoService userBaseInfoService;
    @Resource
    private IUserAccountService userAccountService;
    @Resource
    private UaaFeignClient uaaFeignClient;
    
    // 开启Seata全局事务
    @GlobalTransactional
    @Override
    public JsonResult register(RegisterDto dto) {
        // 校验手机验证码和手机是否注册
        phoneVerification(dto);
        // 远程保存login登录信息
        JsonResult uaaResult = uaaFeignClient.insert(dto);
        // 保存user注册信息
        User user = registerDto2User(dto.getMobile());
        // 设置loginId
        user.setLoginId(ObjectMapperUtils.convertValue(uaaResult.getData(), Long.class));
        insert(user);
        // 保存user_base_info基本信息
        userBaseInfoService.save(user);
        // 保存user_account账户
        userAccountService.save(user);
        // 删除Redis中的手机验证码
        redisTemplate.delete(RedisConstants.REGISTER_CODE_PREFIX + dto.getMobile());
        return JsonResult.success();
    }
    
    /**
     * 手机验证码校验
     *
     * @param dto 注册信息
     */
    private void phoneVerification(RegisterDto dto) {
        String phoneCode = redisTemplate.opsForValue()
                .get(RedisConstants.REGISTER_CODE_PREFIX + dto.getMobile());
        // 过期校验
        VerificationUtils.isNotEmpty(phoneCode, GlobalErrorCode.COMMON_PHONE_VERIFICATION_OVERDUE);
        // 正确校验
        VerificationUtils.isEqualsTrim(RedisUtils.getSmsCode(phoneCode)
                , dto.getSmsCode(), GlobalErrorCode.COMMON_PHONE_VERIFICATION_ERROR);
        // 手机是否已经被注册校验
        VerificationUtils.isNull(selectOne(new EntityWrapper<User>().eq(RedisConstants.PHONE, dto.getMobile()))
                , GlobalErrorCode.USER_PHONE_REPEAT_ERROR);
    }
    
    /**
     * 注册信息转换为user实体
     *
     * @param phone 手机号
     * @return user实体
     */
    private User registerDto2User(String phone) {
        User user = new User();
        user.setCreateTime(new Date().getTime());
        user.setPhone(phone);
        user.setNickName(phone);
        user.setSecLevel(0);
        // 位状态 ： 注册成功
        long bitState = BitStatesUtils.addState(0L, BitStatesUtils.OP_REGISTED);
        // 激活
        bitState = BitStatesUtils.addState(bitState, BitStatesUtils.OP_ACTIVED);
        // 手机认证
        bitState = BitStatesUtils.addState(bitState, BitStatesUtils.OP_AUTHED_PHONE);
        user.setBitState(bitState);
        return user;
    }
}
