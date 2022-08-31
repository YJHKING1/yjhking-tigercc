package org.yjhking.tigercc.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.domain.PayFlow;
import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.service.IPayFlowService;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Component
//支付事务组
@RocketMQTransactionListener(txProducerGroup = MQConstants.TX_PAY_RESULT_GROUP)
@Slf4j
public class PayFlowSaveTransactionListener implements RocketMQLocalTransactionListener {
    @Resource
    private IPayFlowService payFlowService;
    
    //执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            log.info("尝试保存支付流水，执行本地事务.{}", msg);
            byte[] payload = (byte[]) msg.getPayload();
            String json = new String(payload, StandardCharsets.UTF_8);
            log.info("保存支付流水，执行本地事务.{}", json);
            //保存支付账单
            AlipayNotifyDto alipayNotifyDto = JSON.parseObject(json, AlipayNotifyDto.class);
            //幂等处理 ， 如果做了集群，要使用分布式锁
            synchronized (alipayNotifyDto.getOut_trade_no()) {
                //幂等处理
                PayFlow payFlow = payFlowService.selectByOutTradeNo((alipayNotifyDto.getOut_trade_no()));
                if (payFlow == null) {
                    //保存订单到数据库
                    payFlowService.create(alipayNotifyDto);
                }
            }
            //消息推送，直接返回TRUE
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
    
    //检查本地事务状态
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        byte[] payload = (byte[]) msg.getPayload();
        try {
            String json = new String(payload, StandardCharsets.UTF_8);
            log.info("保存支付流水，回查本地事务.{}", json);
            //保存支付账单
            AlipayNotifyDto alipayNotifyDto = JSON.parseObject(json, AlipayNotifyDto.class);
            PayFlow order = payFlowService.selectByOutTradeNo(alipayNotifyDto.getOut_trade_no());
            //回查订单状态
            if (order != null) {
                return RocketMQLocalTransactionState.COMMIT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}