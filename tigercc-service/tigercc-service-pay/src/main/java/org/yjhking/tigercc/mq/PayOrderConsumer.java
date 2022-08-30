package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.domain.PayOrder;
import org.yjhking.tigercc.dto.PayOrder2MQDto;
import org.yjhking.tigercc.service.IPayOrderService;
import org.yjhking.tigercc.utils.VerifyUtils;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstants.TOPIC_ORDER_PAY,
        selectorExpression = MQConstants.TAGS_ORDER_PAY
        , consumerGroup = MQConstants.GROUP_ORDER_PAY
)
public class PayOrderConsumer implements RocketMQListener<MessageExt> {
    
    @Resource
    private IPayOrderService payOrderService;
    
    @Override
    public void onMessage(MessageExt message) {
        if (VerifyUtils.empty(message) || !VerifyUtils.hasLength(message.getBody())) return;
        String msg = new String(message.getBody(), CharsetUtil.UTF_8);
        PayOrder2MQDto dto = JSON.parseObject(msg, PayOrder2MQDto.class);
        // 检查是否重复创建订单
        if (VerifyUtils.nonEmpty(payOrderService.selectOne(new EntityWrapper<PayOrder>().eq(PayOrder.ORDER_NO
                , dto.getOrderNo())))) return;
        PayOrder payOrder = new PayOrder();
        BeanUtils.copyProperties(dto, payOrder);
        payOrder.setCreateTime(new Date());
        payOrder.setPayStatus(PayOrder.STATUS_WAIT_PAY);
        payOrderService.insert(payOrder);
    }
}