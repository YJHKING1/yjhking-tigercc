package org.yjhking.tigercc.service;

import org.yjhking.tigercc.domain.MessageSms;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-15
 */
public interface IMessageSmsService extends IService<MessageSms> {
    
    void saveSmsMessage(String registrationVerificationCode, String smsMessage, String mobile);
}
