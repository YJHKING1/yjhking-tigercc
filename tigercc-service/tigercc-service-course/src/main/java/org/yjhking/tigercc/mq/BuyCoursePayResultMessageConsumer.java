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
import org.yjhking.tigercc.domain.CourseUserLearn;
import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.service.ICourseUserLearnService;
import org.yjhking.tigercc.utils.AssertUtils;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstants.TOPIC_PAY,
        selectorExpression = MQConstants.TAGS_PAY
        , consumerGroup = MQConstants.SERVICE_PAY_PRODUCER
        , messageModel = MessageModel.CLUSTERING)
public class BuyCoursePayResultMessageConsumer implements RocketMQListener<MessageExt> {
    
    @Resource
    private ICourseUserLearnService courseUserLearnService;
    
    //课程订单支付结果处理
    @Override
    public void onMessage(MessageExt message) {
        String msg = new String(message.getBody(), CharsetUtil.UTF_8);
        log.info("课程消费者,处理支付结果收到消息-> {} ", msg);
        AlipayNotifyDto alipayNotifyDto = JSON.parseObject(msg, AlipayNotifyDto.class);
        //幂等处理
        synchronized (this) {
            CourseUserLearn courseUserLearn = courseUserLearnService
                    .selectByCourseOrderNo(alipayNotifyDto.getOut_trade_no());
            //已经保存了用户和课程关系
            if (courseUserLearn != null) return;
        }
        //拿到用户和课程ID：方法有问题
        Map<String, Object> passbackParams = alipayNotifyDto.passbackParams2Map();
        AssertUtils.isNotNull(passbackParams, GlobalErrorCode.COURSE_RECEIPT_PARAMETER_ERROR);
        Long userId = Long.valueOf(passbackParams.get("userId").toString());
        Long courseId = Long.valueOf(passbackParams.get("relationId").toString());
        //保存用户和课程的关系
        courseUserLearnService.create(userId, courseId);
    }
}
