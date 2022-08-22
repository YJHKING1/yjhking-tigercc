package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.domain.MessageStation;
import org.yjhking.tigercc.dto.StationMessage2MQDto;
import org.yjhking.tigercc.mapper.MessageStationMapper;
import org.yjhking.tigercc.service.IMessageStationService;

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
public class MessageStationServiceImpl extends ServiceImpl<MessageStationMapper, MessageStation> implements IMessageStationService {
    
    @Override
    public void saveStationMessages(StationMessage2MQDto stationMessage2MQDto) {
        Date date = new Date();
        stationMessage2MQDto.getUserIds().forEach(userId -> {
            MessageStation messageStation = new MessageStation();
            messageStation.setUserId(userId);
            messageStation.setTitle(stationMessage2MQDto.getTitle());
            messageStation.setContent(stationMessage2MQDto.getContent());
            messageStation.setIsread(NumberConstants.ZERO);
            messageStation.setSendTime(date);
            messageStation.setType(stationMessage2MQDto.getType());
            insert(messageStation);
        });
    }
}
