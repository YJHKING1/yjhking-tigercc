package org.yjhking.tigercc.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.service.IKillCourseService;
import org.yjhking.tigercc.utils.AssertUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author YJH
 */
@Component
@RocketMQMessageListener(
        consumerGroup = MQConstants.KILL_GROUP,
        topic = MQConstants.KIll_TOPIC,
        selectorExpression = MQConstants.KIll_TAGS
)
public class KillExpireConsumer implements RocketMQListener<MessageExt> {
    @Resource
    private IKillCourseService courseService;
    
    @Override
    public void onMessage(MessageExt message) {
        AssertUtils.isNotNull(message, GlobalErrorCode.SERVICE_ERROR);
        String orderKey = new String(message.getBody(), StandardCharsets.UTF_8);
        courseService.cancelPreOrder(orderKey);
    }
}
