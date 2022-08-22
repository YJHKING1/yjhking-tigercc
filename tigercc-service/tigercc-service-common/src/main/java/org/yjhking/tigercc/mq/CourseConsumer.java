package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.dto.StationMessage2MQDto;
import org.yjhking.tigercc.service.IMessageStationService;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = MQConstants.TOPIC_COURSE_FILE, selectorExpression = "*"
        , consumerGroup = "service-Course-consumer", messageModel = MessageModel.BROADCASTING)
@Slf4j
public class CourseConsumer implements RocketMQListener<MessageExt> {
    
    @Resource
    private IMessageStationService messageStationService;
    
    @Override
    public void onMessage(MessageExt message) {
        String jsonString = new String(message.getBody(), CharsetUtil.UTF_8);
        log.info("站内信 消费者 {}", jsonString);
        StationMessage2MQDto stationMessage2MQDto = JSON.parseObject(jsonString, StationMessage2MQDto.class);
        // 保存站内信
        messageStationService.saveStationMessages(stationMessage2MQDto);
        log.info("站内信 消费者处理完成");
    }
}
