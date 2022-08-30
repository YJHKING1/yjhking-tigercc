package org.yjhking.tigercc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseMarket;
import org.yjhking.tigercc.domain.CourseOrder;
import org.yjhking.tigercc.domain.CourseOrderItem;
import org.yjhking.tigercc.dto.PayOrder2MQDto;
import org.yjhking.tigercc.dto.PlaceOrderDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.feignclient.CourseFeignClient;
import org.yjhking.tigercc.mapper.CourseOrderMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ICourseOrderItemService;
import org.yjhking.tigercc.service.ICourseOrderService;
import org.yjhking.tigercc.utils.AssertUtils;
import org.yjhking.tigercc.utils.CodeGenerateUtils;
import org.yjhking.tigercc.vo.CourseDataOrderVo;
import org.yjhking.tigercc.vo.CourseItemDataOrderVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderMapper, CourseOrder> implements ICourseOrderService {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private CourseFeignClient courseFeignClient;
    @Resource
    private ICourseOrderItemService orderItemService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    
    @Override
    public String placeOrder(PlaceOrderDto dto) {
        // todo 假数据 用户id
        Long loginId = 3L;
        // 参数校验
        String courseIdStr = StringUtils.join(dto.getCourseIds(), TigerccConstants.SEPARATOR);
        String token = (String) redisTemplate.opsForValue()
                .get(RedisConstants.TOKEN + loginId + RedisConstants.REDIS_VERIFY + courseIdStr);
        AssertUtils.isHasLength(token, GlobalErrorCode.ORDER_REPEAT);
        AssertUtils.isEquals(token, dto.getToken(), GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 获取课程数据
        JsonResult result = courseFeignClient.selectCourseDataForOrder(courseIdStr);
        AssertUtils.isTrue(result.isSuccess(), GlobalErrorCode.COURSE_ERROR);
        AssertUtils.isNotNull(result.getData(), GlobalErrorCode.COURSE_ERROR);
        CourseDataOrderVo courseDataOrderVo =
                JSON.parseObject(JSON.toJSONString(result.getData()), CourseDataOrderVo.class);
        BigDecimal totalAmount = courseDataOrderVo.getTotalAmount();
        List<CourseItemDataOrderVo> courseInfos = courseDataOrderVo.getCourseInfos();
        // 构建明细
        Date now = new Date();
        String orderSn = CodeGenerateUtils.generateOrderSn(loginId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TigerccConstants.ORDER_BUY);
        int totalCount = NumberConstants.ZERO;
        List<CourseOrderItem> items = new ArrayList<>(courseInfos.size());
        for (CourseItemDataOrderVo courseItem : courseInfos) {
            Course course = courseItem.getCourse();
            CourseMarket courseMarket = courseItem.getCourseMarket();
            CourseOrderItem item = new CourseOrderItem();
            item.setAmount(courseMarket.getPrice());
            item.setCount(NumberConstants.ONE);
            totalCount = totalCount + item.getCount();
            item.setCourseId(course.getId());
            item.setCourseName(course.getName());
            item.setCoursePic(course.getPic());
            item.setCreateTime(now);
            item.setOrderNo(orderSn);
            item.setSubtotalAmount(courseMarket.getPrice().multiply(new BigDecimal(item.getCount())));
            stringBuilder.append(course.getName()).append(TigerccConstants.SEMICOLON);
            items.add(item);
        }
        stringBuilder.append(TigerccConstants.ORDER_PAY).append(totalAmount).append(TigerccConstants.ORDER_YUAN);
        // 保存订单
        CourseOrder courseOrder = new CourseOrder();
        courseOrder.setCreateTime(now);
        courseOrder.setDiscountAmount(new BigDecimal(NumberConstants.ZERO));
        courseOrder.setOrderNo(orderSn);
        courseOrder.setOrderType(dto.getType());
        courseOrder.setPayType(dto.getPayType());
        courseOrder.setTotalAmount(totalAmount);
        courseOrder.setPayAmount(courseOrder.getTotalAmount().subtract(courseOrder.getDiscountAmount()));
        courseOrder.setStatusOrder(CourseOrder.STATE_WAIT_PAY);
        courseOrder.setTitle(stringBuilder.toString());
        courseOrder.setTotalCount(totalCount);
        courseOrder.setUserId(loginId);
        courseOrder.setItems(items);
        // 保存订单
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(
                TigerccConstants.MQ_COURSEORDER_PAY_GROUP_TRANSACTION, MQConstants.TOPIC_PAYORDER_TAGS_PAYORDER
                , MessageBuilder.withPayload(JSON.toJSONString(new PayOrder2MQDto(courseOrder.getPayAmount()
                                , courseOrder.getPayType(), orderSn, loginId, "", courseOrder.getTitle())))
                        .build(), courseOrder);
        // 断言下单状态
        AssertUtils.isEquals(transactionSendResult.getLocalTransactionState()
                , LocalTransactionState.COMMIT_MESSAGE, GlobalErrorCode.ORDER_MISS);
        // 断言发送事务消息状态
        AssertUtils.isEquals(transactionSendResult.getSendStatus()
                , SendStatus.SEND_OK, GlobalErrorCode.SERVICE_TRANSACTION_MESSAGE_FAILED);
        // 删除token，防止重复提交
        redisTemplate.delete(token);
        return orderSn;
    }
    
    @Override
    public void saveOrderAndItem(CourseOrder courseOrder) {
        AssertUtils.isNull(selectByOrderNo(courseOrder.getOrderNo()), GlobalErrorCode.ORDER_EXIST);
        insert(courseOrder);
        // 保存明细
        courseOrder.getItems().forEach(item -> item.setOrderId(courseOrder.getId()));
        orderItemService.insertBatch(courseOrder.getItems());
    }
    
    public CourseOrder selectByOrderNo(String orderNo) {
        return selectOne(new EntityWrapper<CourseOrder>().eq(CourseOrder.ORDER_NO, orderNo));
    }
}
