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
import org.yjhking.tigercc.domain.CourseOrder;
import org.yjhking.tigercc.domain.PayOrder;
import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.service.ICourseOrderService;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstants.TOPIC_PAY,
        selectorExpression = MQConstants.TAGS_PAY
        , consumerGroup = MQConstants.SERVICE_PAY_PRODUCER
        , messageModel = MessageModel.CLUSTERING
)
public class BuyCoursePayResultMessageConsumer implements RocketMQListener<MessageExt> {
    
    @Resource
    private ICourseOrderService courseOrderService;
    
    @Override
    public void onMessage(MessageExt message) {
        String msg = new String(message.getBody(), CharsetUtil.UTF_8);
        log.info("订单消费者,处理支付结果收到消息-> {} ", msg);
        AlipayNotifyDto dto = JSON.parseObject(msg, AlipayNotifyDto.class);
        CourseOrder order = courseOrderService.selectByOrderNo(dto.getOut_trade_no());
        //幂等判断,如果是集群需要加分布式锁
        synchronized (order.getOrderNo()) {
            //课程订单已经被处理
            if (order.getStatusOrder() != PayOrder.STATUS_PAY_SUCCESS) {
                return;
            }
        }
        //支付结果修改
        if (dto.isTradeSuccess()) {
            order.setStatusOrder(PayOrder.STATUS_PAY_SUCCESS);
            order.setUpdateTime(new Date());
        } else {
            order.setStatusOrder(PayOrder.STATUS_AUTO_CANCEL);
        }
        courseOrderService.updateById(order);
    }
}