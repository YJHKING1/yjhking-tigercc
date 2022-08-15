package org.yjhking.tigercc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.TitleConstants;
import org.yjhking.tigercc.constants.VerifyCodeConstants;
import org.yjhking.tigercc.dto.MobileCodeDto;
import org.yjhking.tigercc.dto.SMSVerifyCodeProperties;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IMessageSmsService;
import org.yjhking.tigercc.service.IVerifycodeService;
import org.yjhking.tigercc.utils.StrUtils;

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
        // todo 图片验证码校验
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
