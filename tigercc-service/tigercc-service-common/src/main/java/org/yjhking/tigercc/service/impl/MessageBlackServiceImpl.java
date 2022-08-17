package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.VerifyCodeConstants;
import org.yjhking.tigercc.domain.MessageBlack;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.dto.BlackDto;
import org.yjhking.tigercc.mapper.MessageBlackMapper;
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
 * @since 2022-08-17
 */
@Service
public class MessageBlackServiceImpl extends ServiceImpl<MessageBlackMapper, MessageBlack> implements IMessageBlackService {
    @Resource
    private IMessageSmsService messageSmsService;
    
    @Override
    public void save(MessageSms messageSms, BlackDto dto) {
        MessageBlack messageBlack = new MessageBlack();
        messageBlack.setBlackTime(new Date());
        messageBlack.setIp(messageSms.getIp());
        messageBlack.setPhone(dto.getPhone());
        messageBlack.setUserId(messageSms.getUserId());
        messageBlack.setReason(dto.getMsg());
        insert(messageBlack);
    }
    
    @Override
    public List<MessageBlack> selectBlack(String ip, String phone) {
        // 查userId
        EntityWrapper<MessageSms> smsQuery = new EntityWrapper<>();
        smsQuery.eq(VerifyCodeConstants.PHONE, phone);
        List<MessageSms> messageSmsList = messageSmsService.selectList(smsQuery);
        MessageSms messageSms = null;
        if (VerificationUtils.listVerification(messageSmsList)) {
            messageSms = messageSmsList.get(messageSmsList.size() - NumberConstants.ONE);
        }
        EntityWrapper<MessageBlack> query = new EntityWrapper<>();
        // 根据ip或手机及userId查找数据库
        if (VerificationUtils.objectVerification(messageSms)) query.eq(VerifyCodeConstants.IP, ip)
                .or().eq(VerifyCodeConstants.PHONE, phone).or().eq(VerifyCodeConstants.USER_ID, messageSms.getUserId());
            // 根据ip或手机查找数据库
        else query.eq(VerifyCodeConstants.IP, ip).or().eq(VerifyCodeConstants.PHONE, phone);
        return selectList(query);
    }
    
    @Override
    public void saveBlack(BlackDto dto) {
        MessageBlack messageBlack = new MessageBlack();
        messageBlack.setPhone(dto.getPhone());
        messageBlack.setReason(dto.getMsg());
        messageBlack.setBlackTime(new Date());
        insert(messageBlack);
    }
    
    @Override
    public void updateBlack(BlackDto dto) {
        MessageBlack messageBlack = new MessageBlack();
        messageBlack.setId(dto.getId());
        messageBlack.setPhone(dto.getPhone());
        messageBlack.setReason(dto.getMsg());
        messageBlack.setBlackTime(new Date());
        updateById(messageBlack);
    }
}
