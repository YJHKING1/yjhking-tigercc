package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.MessageBlack;
import org.yjhking.tigercc.domain.MessageSms;
import org.yjhking.tigercc.dto.BlackDto;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-17
 */
public interface IMessageBlackService extends IService<MessageBlack> {
    
    void save(MessageSms messageSms, BlackDto dto);
    
    List<MessageBlack> selectBlack(String ip, String phone);
    
    void saveBlack(BlackDto dto);
    
    void updateBlack(BlackDto dto);
}
