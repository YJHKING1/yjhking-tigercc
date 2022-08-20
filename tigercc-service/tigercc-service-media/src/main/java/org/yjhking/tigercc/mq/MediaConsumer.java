package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.domain.MediaFile;
import org.yjhking.tigercc.service.IMediaFileService;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = MediaProducer.mediaTopic, selectorExpression = "*"
        , consumerGroup = "service-media-consumer", messageModel = MessageModel.BROADCASTING)
@Slf4j
public class MediaConsumer implements RocketMQListener<MessageExt> {
    
    @Resource
    private IMediaFileService mediaFileService;
    
    @Override
    public void onMessage(MessageExt message) {
        String mediaFileJSON = new String(message.getBody(), CharsetUtil.UTF_8);
        log.info("MediaFile 消费者 {}", mediaFileJSON);
        MediaFile mediaFile = JSON.parseObject(mediaFileJSON, MediaFile.class);
        //把文件转换成 m3u8 格式，并上传到文件服务器
        mediaFileService.handleFile2m3u8(mediaFile);
        log.info("MediaFile 消费者处理完成");
    }
}
