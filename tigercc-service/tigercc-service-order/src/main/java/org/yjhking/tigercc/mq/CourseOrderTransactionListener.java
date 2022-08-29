package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.CourseOrder;
import org.yjhking.tigercc.domain.PayOrder;
import org.yjhking.tigercc.service.ICourseOrderService;

import java.nio.charset.StandardCharsets;

// 保存课程订单以及支付订单的事务消息
@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = TigerccConstants.MQ_COURSEORDER_PAY_GROUP_TRANSACTION)
public class CourseOrderTransactionListener implements RocketMQLocalTransactionListener {
    
    @Autowired
    private ICourseOrderService courseOrderService;
    
    // 执行本地事务 - 保存订单 ； message：消息内容 ; args :额外参数
    // 1.在这里保存订单：需要订单数据(订单+订单明细)
    // 2.需要把数据发送到MQ用作保存支付单？
    // 3.请问：到底发送什么数据能适应上面2总场景
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object args) {
        log.info("事务监听器 message = {} ", message.getPayload());
        log.info("事务监听器 args = {}", args);
        if (message == null || message.getPayload() == null || args == null) {
            log.error("事务监听器 拿到的数据有问题 {} ,{}", message, args);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        try {
            // 课程订单
            CourseOrder courseOrder = (CourseOrder) args;
            // 保存课程订单和明细
            courseOrderService.saveOrderAndItem(courseOrder);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("事务消息回滚");
        return RocketMQLocalTransactionState.ROLLBACK;
    }
    
    // 检查本地事务 - 检查订单是否超过
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        byte[] bytes = (byte[]) message.getPayload();
        if (message == null || message.getPayload() == null) return RocketMQLocalTransactionState.ROLLBACK;
        // 支付单数据，也是要发给MQ的消息
        try {
            String orderJSON = new String(bytes, StandardCharsets.UTF_8);
            log.info("支付订单{}", orderJSON);
            PayOrder payOrder = JSON.parseObject(orderJSON, PayOrder.class);
            // 根据订单号查询订单
            CourseOrder courseOrderFromDB = courseOrderService.selectOne(new EntityWrapper<CourseOrder>()
                    .eq(CourseOrder.ORDER_NO, payOrder.getOrderNo()));
            if (courseOrderFromDB != null) {
                log.info("本地事务检查，订单存在，提交");
                return RocketMQLocalTransactionState.COMMIT;
            }
        } catch (Exception e) {
            log.error("本地事务检查，出现异常 {}", e.getMessage());
            e.printStackTrace();
        }
        log.info("本地事务检查，订单不存在，回滚");
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
