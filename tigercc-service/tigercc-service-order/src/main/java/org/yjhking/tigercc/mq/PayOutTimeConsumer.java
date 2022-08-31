package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.dto.PlaceCourseOrderTo;
import org.yjhking.tigercc.service.ICourseOrderService;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstants.TOPIC_COURSE_ORDER_DEALY,
        selectorExpression = MQConstants.TAGS_COURSE_ORDER_DEALY
        , consumerGroup = MQConstants.SERVICE_PAY_PRODUCER
        , messageModel = MessageModel.BROADCASTING
)
public class PayOutTimeConsumer implements RocketMQListener<MessageExt> {
    
    @Autowired
    private ICourseOrderService courseOrderService;
    
    @Override
    public void onMessage(MessageExt message) {
        String msg = new String(message.getBody(), CharsetUtil.UTF_8);
        log.info("支付超时消费者, {} ", msg);
        PlaceCourseOrderTo placeCourseOrderTo = JSON.parseObject(msg, PlaceCourseOrderTo.class);
        //支付超时关单
        courseOrderService.closeOrder(placeCourseOrderTo);
    }
}