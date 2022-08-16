package org.yjhking.tigercc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.TitleConstants;
import org.yjhking.tigercc.constants.VerifyCodeConstants;
import org.yjhking.tigercc.dto.IMGVerifyCodeProperties;
import org.yjhking.tigercc.dto.MobileCodeDto;
import org.yjhking.tigercc.dto.SMSVerifyCodeProperties;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IMessageSmsService;
import org.yjhking.tigercc.service.IVerifycodeService;
import org.yjhking.tigercc.utils.StrUtils;
import org.yjhking.tigercc.utils.VerificationUtils;
import org.yjhking.tigercc.utils.VerifyCodeUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author YJH
 */
@Service
@Slf4j
public class VerifycodeServiceImpl implements IVerifycodeService {
    /**
     * 短信验证码配置
     */
    @Resource
    private SMSVerifyCodeProperties smsProperties;
    /**
     * 图片验证码配置
     */
    @Resource
    private IMGVerifyCodeProperties imgProperties;
    /**
     * redis
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 短信服务
     */
    @Resource
    private IMessageSmsService messageSmsService;
    
    @Override
    public JsonResult sendSmsCode(MobileCodeDto mobileCodeDto) {
        // 图片验证码校验
        imgVerification(mobileCodeDto);
        // 校验并发送短信验证码
        String smsMessage = sendCode(mobileCodeDto.getMobile(), codeVerification(mobileCodeDto));
        // 保存到数据库
        messageSmsService.saveSmsMessage(TitleConstants.REGISTRATION_VERIFICATION_CODE, smsMessage
                , mobileCodeDto.getMobile());
        return new JsonResult();
    }
    
    @Override
    public JsonResult imageCode(String key) {
        // 判断UUID不为空
        VerificationUtils.isNotEmpty(key, GlobalErrorCode.COMMON_IMG_VERIFICATION_NULL);
        // 生成随机验证码
        String code = VerifyCodeUtils.generateVerifyCode(imgProperties.getLength());
        // 把验证码的值存储到Redis,以前台传入的UUID作为key
        redisTemplate.opsForValue().set(key, code, imgProperties.getExpire(), TimeUnit.MINUTES);
        // 图片验证码宽度
        int codeImgWidth = VerifyCodeConstants.CODE_IMG_WIDTH;
        // 图片验证码高度
        int codeImgHeight = VerifyCodeConstants.CODE_IMG_HEIGHT;
        // 把验证码的值转为base64格式的图片，并返回
        return JsonResult.success(VerifyCodeUtils.verifyCode(codeImgWidth, codeImgHeight, code));
    }
    
    /**
     * 图片验证码校验
     *
     * @param mobileCodeDto 用户输入的验证码
     */
    private void imgVerification(MobileCodeDto mobileCodeDto) {
        String imageCodeKey = redisTemplate.opsForValue().get(mobileCodeDto.getImageCodeKey());
        // 判断图片验证码是否过期
        VerificationUtils.isNotEmpty(imageCodeKey, GlobalErrorCode.COMMON_IMG_VERIFICATION_OVERDUE);
        // 判断图片验证码是否正确
        VerificationUtils.isEqualsIgnoreCase(imageCodeKey, mobileCodeDto.getImageCode()
                , GlobalErrorCode.COMMON_IMG_VERIFICATION_ERROR);
    }
    
    /**
     * 短信验证码过期与重发校验
     *
     * @param mobileCodeDto 用户输入的验证码
     * @return 验证码
     */
    private String codeVerification(MobileCodeDto mobileCodeDto) {
        String codeString = redisTemplate.opsForValue()
                .get(VerifyCodeConstants.REGISTER_CODE_PREFIX + mobileCodeDto.getMobile());
        String code;
        // 校验是否过期
        if (VerificationUtils.stringVerification(codeString)) {
            // todo 封装redis方法
            String time = codeString.split(VerifyCodeConstants.REDIS_VERIFY)[VerifyCodeConstants.REDIS_VERIFY_SECOND];
            long intervalTime = System.currentTimeMillis() - Long.parseLong(time);
            // 判断是否在60秒内重复发送
            if (intervalTime <= smsProperties.getInterval() * NumberConstants.THOUSAND)
                throw new GlobalCustomException(GlobalErrorCode.COMMON_VERIFICATION_REPEAT_SEND);
                // 重新发送验证码
                // todo 封装redis方法
            else code = codeString.split(VerifyCodeConstants.REDIS_VERIFY)[VerifyCodeConstants.REDIS_VERIFY_FIRST];
            // 创建新的验证码
        } else code = StrUtils.getRandomString(smsProperties.getLength());
        // 保存验证码到Redis，格式为k：前缀+手机号；v：验证码:创建时间，并设置过期时间
        redisTemplate.opsForValue().set(VerifyCodeConstants.REGISTER_CODE_PREFIX + mobileCodeDto.getMobile(),
                code + VerifyCodeConstants.REDIS_VERIFY + System.currentTimeMillis()
                , smsProperties.getExpire(), TimeUnit.MINUTES);
        return code;
    }
    
    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return 短信内容
     */
    private String sendCode(String mobile, String code) {
        // 发短信
        String smsCode = String.format(smsProperties.getMessage(), code, smsProperties.getExpire());
        // smsCode = SmsUtils.sendSms(mobile, smsCode);
        // 测试
        log.info(smsCode);
        return smsCode;
    }
}
