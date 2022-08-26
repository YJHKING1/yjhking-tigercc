package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.dto.BlackDto;
import org.yjhking.tigercc.mapper.MessageSmsMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IMessageBlackService;
import org.yjhking.tigercc.service.IMessageSmsService;
import org.yjhking.tigercc.utils.VerificationUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    @Resource
    private IMessageBlackService messageBlackService;
    
    @Override
    public void saveSmsMessage(String registrationVerificationCode, String smsMessage, String mobile) {
        // 保存短信到数据库
        MessageSms messageSms = new MessageSms();
        messageSms.setTitle(registrationVerificationCode);
        messageSms.setContent(smsMessage);
        messageSms.setSendTime(new Date());
        messageSms.setPhone(mobile);
        // ip保存
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (VerificationUtils.isValid(requestAttributes))
            messageSms.setIp(requestAttributes.getRequest().getRemoteAddr());
        super.insert(messageSms);
    }
    
    @Override
    public JsonResult black(BlackDto dto) {
        VerificationUtils.isNotNull(dto);
        List<MessageSms> messageSmsList = selectList(new EntityWrapper<MessageSms>()
                .eq(RedisConstants.PHONE, dto.getPhone()));
        // 最新的数据
        MessageSms messageSms = messageSmsList.get(messageSmsList.size() - NumberConstants.ONE);
        messageBlackService.save(messageSms, dto);
        return JsonResult.success();
    }
}
