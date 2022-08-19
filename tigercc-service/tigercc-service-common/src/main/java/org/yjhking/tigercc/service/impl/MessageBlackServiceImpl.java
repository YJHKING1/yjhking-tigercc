package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.domain.MessageBlack;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.dto.BlackDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.MessageBlackMapper;
import org.yjhking.tigercc.service.IMessageBlackService;
import org.yjhking.tigercc.service.IMessageSmsService;
import org.yjhking.tigercc.utils.VerificationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
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
        // 重复添加校验
        repeatCheck(messageSms.getIp(), dto.getPhone());
        MessageBlack messageBlack = new MessageBlack();
        messageBlack.setBlackTime(new Date());
        messageBlack.setIp(messageSms.getIp());
        messageBlack.setPhone(dto.getPhone());
        messageBlack.setUserId(messageSms.getUserId());
        messageBlack.setReason(dto.getMsg());
        insert(messageBlack);
    }
    
    /**
     * 重复添加校验
     *
     * @param ip    ip
     * @param phone 手机号
     */
    private void repeatCheck(String ip, String phone) {
        List<MessageBlack> list = selectList(new EntityWrapper<MessageBlack>().eq(RedisConstants.IP, ip)
                .or().eq(RedisConstants.PHONE, phone));
        VerificationUtils.listIsNull(list, GlobalErrorCode.COMMON_VERIFICATION_BLACK);
    }
    
    @Override
    // 添加redis缓存
    @Cacheable(value = RedisConstants.BLACK_LIST_KEY, key = RedisConstants.BLACK_LIST)
    public List<MessageBlack> selectBlack(String ip, String phone) {
        // 查userId
        List<MessageSms> messageSmsList = messageSmsService
                .selectList(new EntityWrapper<MessageSms>().eq(RedisConstants.PHONE, phone));
        MessageSms messageSms = null;
        if (VerificationUtils.listVerification(messageSmsList))
            messageSms = messageSmsList.get(messageSmsList.size() - NumberConstants.ONE);
        EntityWrapper<MessageBlack> query = new EntityWrapper<>();
        // 根据ip或手机及userId查找数据库
        if (VerificationUtils.objectVerification(messageSms)) query.eq(RedisConstants.IP, ip)
                .or().eq(RedisConstants.PHONE, phone).or().eq(RedisConstants.USER_ID, messageSms.getUserId());
            // 根据ip或手机查找数据库
        else query.eq(RedisConstants.IP, ip).or().eq(RedisConstants.PHONE, phone);
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
    
    @Override
    // 删除redis缓存
    @CacheEvict(value = RedisConstants.BLACK_LIST_KEY, key = RedisConstants.BLACK_LIST)
    public boolean insert(MessageBlack entity) {
        return super.insert(entity);
    }
    
    @Override
    @CacheEvict(value = RedisConstants.BLACK_LIST_KEY, key = RedisConstants.BLACK_LIST)
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }
    
    @Override
    @CacheEvict(value = RedisConstants.BLACK_LIST_KEY, key = RedisConstants.BLACK_LIST)
    public boolean updateById(MessageBlack entity) {
        return super.updateById(entity);
    }
}
