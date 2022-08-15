package org.yjhking.tigercc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
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
import org.yjhking.tigercc.utils.VerifyCodeUtils;

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
    @Autowired
    private SMSVerifyCodeProperties smsProperties;
    /**
     * 图片验证码配置
     */
    @Autowired
    private IMGVerifyCodeProperties imgProperties;
    /**
     * redis
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 短信服务
     */
    @Autowired
    private IMessageSmsService messageSmsService;
    
    @Override
    public JsonResult sendSmsCode(MobileCodeDto mobileCodeDto) {
        // 图片验证码校验
        String imageCodeKeyTrue = (String) redisTemplate.opsForValue().get(mobileCodeDto.getImageCodeKey());
        if (imageCodeKeyTrue == null || imageCodeKeyTrue.trim().length() == 0) {
            throw new GlobalCustomException(GlobalErrorCode.COMMON_IMG_VERIFICATION_OVERDUE);
        }
        if (!imageCodeKeyTrue.equalsIgnoreCase(mobileCodeDto.getImageCode())) {
            throw new GlobalCustomException(GlobalErrorCode.COMMON_IMG_VERIFICATION_ERROR);
        }
        // 判断是否有未过期验证码
        String codeString = (String) redisTemplate.opsForValue()
                .get(VerifyCodeConstants.REGISTER_CODE_PREFIX + mobileCodeDto.getMobile());
        String code = null;
        if (codeString != null && codeString.trim().length() > 0) {
            String time = codeString.split(":")[1];
            long intervalTime = System.currentTimeMillis() - Long.parseLong(time);
            // 判断是否在60秒内重复发送
            if (intervalTime <= smsProperties.getInterval() * 1000L) {
                throw new GlobalCustomException(GlobalErrorCode.COMMON_VERIFICATION_REPEAT_SEND);
            } else {
                code = codeString.split(":")[0];
            }
        } else {
            // 创建新的验证码
            code = StrUtils.getRandomString(smsProperties.getLength());
        }
        // 保存验证码到Redis，格式为k：前缀+手机号；v：验证码+创建时间，并设置过期时间
        redisTemplate.opsForValue().set(VerifyCodeConstants.REGISTER_CODE_PREFIX + mobileCodeDto.getMobile(),
                code + ":" + System.currentTimeMillis(), smsProperties.getExpire(), TimeUnit.MINUTES);
        // 发送短信验证码
        String smsMessage = sendCode(mobileCodeDto.getMobile(), code);
        // 保存到数据库
        messageSmsService.saveSmsMessage(TitleConstants.REGISTRATION_VERIFICATION_CODE, smsMessage
                , mobileCodeDto.getMobile());
        return new JsonResult();
    }
    
    @Override
    public JsonResult imageCode(String key) {
        // 判断UUID不为空
        if (key == null || key.trim().length() == 0) {
            throw new GlobalCustomException(GlobalErrorCode.COMMON_IMG_VERIFICATION_NULL);
        }
        // 生成随机验证码
        String code = VerifyCodeUtils.generateVerifyCode(imgProperties.getLength());
        // 把验证码的值存储到Redis,以前台传入的UUID作为key
        redisTemplate.opsForValue().set(key, code, imgProperties.getExpire(), TimeUnit.MINUTES);
        // 图片验证码宽度
        int codeImgWidth = 115;
        // 图片验证码高度
        int codeImgHeight = 40;
        // 把验证码的值转为base64格式的图片，并返回
        return JsonResult.success(VerifyCodeUtils.verifyCode(codeImgWidth, codeImgHeight, code));
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
        // String smsCode = SmsUtils.sendSms(mobile, "金虎云课堂：您的验证码为：" + code + "请在10分钟内使用");
        // 测试
        String smsCode = "手机：" + mobile + " 验证码：" + code + " 请在10分钟内使用";
        log.info(smsCode);
        return smsCode;
    }
}
