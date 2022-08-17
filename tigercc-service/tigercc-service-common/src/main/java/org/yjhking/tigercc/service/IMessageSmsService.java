package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.dto.BlackDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-15
 */
public interface IMessageSmsService extends IService<MessageSms> {
    
    void saveSmsMessage(String registrationVerificationCode, String smsMessage, String mobile);
    
    JsonResult black(BlackDto dto);
}
