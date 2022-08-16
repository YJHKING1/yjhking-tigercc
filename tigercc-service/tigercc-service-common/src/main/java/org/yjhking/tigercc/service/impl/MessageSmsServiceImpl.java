package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.mapper.MessageSmsMapper;
import org.yjhking.tigercc.service.IMessageSmsService;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-15
 */
@Service
public class MessageSmsServiceImpl extends ServiceImpl<MessageSmsMapper, MessageSms> implements IMessageSmsService {
    
    @Override
    public void saveSmsMessage(String registrationVerificationCode, String smsMessage, String mobile) {
        // 保存短信到数据库
        MessageSms messageSms = new MessageSms();
        messageSms.setTitle(registrationVerificationCode);
        messageSms.setContent(smsMessage);
        messageSms.setSendTime(new Date());
        messageSms.setPhone(mobile);
        // todo ip保存
        super.insert(messageSms);
    }
}
