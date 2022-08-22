package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.MessageStation;
import org.yjhking.tigercc.dto.StationMessage2MQDto;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-15
 */
public interface IMessageStationService extends IService<MessageStation> {
    
    void saveStationMessages(StationMessage2MQDto stationMessage2MQDto);
}
