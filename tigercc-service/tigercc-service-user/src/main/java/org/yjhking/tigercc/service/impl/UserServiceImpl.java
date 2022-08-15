package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.VerifyCodeConstants;
import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
import org.yjhking.tigercc.mapper.UserMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IUserAccountService;
import org.yjhking.tigercc.service.IUserBaseInfoService;
import org.yjhking.tigercc.service.IUserService;
import org.yjhking.tigercc.utils.BitStatesUtils;

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
    @Autowired
    private IUserBaseInfoService userBaseInfoService;
    @Autowired
    private IUserAccountService userAccountService;
    
    @Override
    public JsonResult register(RegisterDto dto) {
        // 手机验证码校验
        String phoneCode = (String) redisTemplate.opsForValue()
                .get(VerifyCodeConstants.REGISTER_CODE_PREFIX + dto.getMobile());
        if (phoneCode == null || phoneCode.trim().length() == 0) {
            throw new GlobalCustomException(GlobalErrorCode.COMMON_PHONE_VERIFICATION_OVERDUE);
        }
        if (!phoneCode.split(":")[0].equals(dto.getSmsCode())) {
            throw new GlobalCustomException(GlobalErrorCode.COMMON_PHONE_VERIFICATION_ERROR);
        }
        // 手机是否已经被注册校验
        EntityWrapper<User> query = new EntityWrapper<>();
        query.eq("phone", dto.getMobile());
        User user = selectOne(query);
        if (user != null) {
            throw new GlobalCustomException(GlobalErrorCode.USER_PHONE_REPEAT_ERROR);
        }
        // todo 远程保存login登录信息
        // 保存user注册信息
        user = registerDto2User(dto.getMobile());
        insert(user);
        // 保存user_base_info基本信息
        userBaseInfoService.save(user);
        // 保存user_account账户
        userAccountService.save(user);
        // 删除Redis中的手机验证码
        redisTemplate.delete(VerifyCodeConstants.REGISTER_CODE_PREFIX + dto.getMobile());
        return JsonResult.success();
    }
    
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
