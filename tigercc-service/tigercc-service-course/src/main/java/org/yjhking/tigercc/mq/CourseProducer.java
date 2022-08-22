package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.domain.MessageStation;

import javax.annotation.Resource;

//生产者
@Component
@Slf4j
public class CourseProducer {
    @Resource
    private RocketMQTemplate template;
    
    public boolean synSend(MessageStation messageStation) {
        try {
            String jsonString = JSON.toJSONString(messageStation);
            SendResult result = template.syncSend(MQConstants.TOPIC_COURSE_FILE
                    , MessageBuilder.withPayload(jsonString).build());
            log.info("发送站内信到MQ处理 站内信 = {}", messageStation);
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}