package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.domain.MediaFile;

import javax.annotation.Resource;

//生产者
@Component
@Slf4j
public class MediaProducer {
    @Resource
    private RocketMQTemplate template;
    public static final String mediaTopic = "service-media-producer";
    
    public boolean synSend(MediaFile mediaFile) {
        try {
            String mediaFileJSON = JSON.toJSONString(mediaFile);
            SendResult result = template.syncSend(mediaTopic, MessageBuilder.withPayload(mediaFileJSON).build());
            log.info("媒体文件发送到MQ处理 mediaFile = {}", mediaFile);
            return result.getSendStatus() == SendStatus.SEND_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}