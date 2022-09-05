package org.yjhking.tigercc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
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
import org.yjhking.tigercc.dto.*;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
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
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
@Slf4j
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderMapper, CourseOrder> implements ICourseOrderService {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private CourseFeignClient courseFeignClient;
    @Resource
    private ICourseOrderItemService orderItemService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private RedissonClient redissonClient;
    
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
                MQConstants.MQ_COURSEORDER_PAY_GROUP_TRANSACTION, MQConstants.TOPIC_PAYORDER_TAGS_PAYORDER
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
    
    @Override
    public void closeOrder(PlaceCourseOrderTo placeCourseOrderTo) {
        String orderNo = placeCourseOrderTo.getOrderNo();
        CourseOrder courseOrder = selectByOrderNo(orderNo);
        AssertUtils.isNotNull(courseOrder, GlobalErrorCode.ORDER_TIME_ERROR);
        if (courseOrder.getStatusOrder() == CourseOrder.STATE_WAIT_PAY) {
            courseOrder.setStatusOrder(CourseOrder.STATE_AUTO_CANCEL);
            courseOrder.setUpdateTime(new Date());
            updateById(courseOrder);
            if (Objects.equals(courseOrder.getOrderType(), NumberConstants.ONE)) {
                // 退库存
                redissonClient.getSemaphore(RedisConstants.STORE + orderItemService.selectList(
                                new EntityWrapper<CourseOrderItem>().eq(CourseOrder.ORDER_NO, orderNo))
                        .get(NumberConstants.ZERO).getKillCourseId()).release(courseOrder.getTotalCount());
            }
        }
        log.info("支付关单申请...");
        //查询ali支付配置参数
        // AlipayInfo alipayInfo = alipayInfoService.selectList(null).get(0);
        //配置对象
        // Config config = getOptions(alipayInfo);
        // Factory.setOptions(config);
        try {
            // 2. 发起API调用
            AlipayTradeCloseResponse response = Factory.Payment.Common().close(orderNo);
            if (response.code.equals("10000")) {
                log.info("关单成功");
            } else {
                log.info("关单失败 {} , {} ", response.msg, response.subMsg);
            }
        } catch (Exception e) {
            log.error("支付申请异常 {}", e.getMessage());
            throw new GlobalCustomException(GlobalErrorCode.SERVICE_TRANSACTION_MESSAGE_FAILED);
        }
    }
    
    @Override
    public JsonResult killPlaceOrder(CourseKillOrderParamDto dto) {
        // todo 假数据 用户id
        Long loginId = 3L;
        PreCourseOrder2RedisDto order2RedisDto = (PreCourseOrder2RedisDto) redisTemplate.opsForValue().get(
                RedisConstants.ORDER + loginId + RedisConstants.REDIS_VERIFY + dto.getOrderNo());
        AssertUtils.isNotNull(order2RedisDto, GlobalErrorCode.ORDER_NUM_ERROR);
        // 参数校验
        String token = (String) redisTemplate.opsForValue().get(
                RedisConstants.TOKEN + loginId + order2RedisDto.getCourseId());
        AssertUtils.isHasLength(token, GlobalErrorCode.ORDER_REPEAT);
        AssertUtils.isEquals(token, dto.getToken(), GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 构建明细
        Date now = new Date();
        String orderSn = dto.getOrderNo();
        CourseOrderItem courseOrderItem = new CourseOrderItem();
        courseOrderItem.setAmount(order2RedisDto.getAmount());
        courseOrderItem.setCount(order2RedisDto.getCount());
        courseOrderItem.setCreateTime(now);
        courseOrderItem.setCourseId(order2RedisDto.getCourseId());
        courseOrderItem.setCourseName(order2RedisDto.getCourseName());
        courseOrderItem.setCoursePic(order2RedisDto.getCoursePic());
        courseOrderItem.setOrderNo(orderSn);
        courseOrderItem.setSubtotalAmount(courseOrderItem.getAmount().multiply(
                new BigDecimal(courseOrderItem.getCount())));
        // 保存订单
        CourseOrder courseOrder = new CourseOrder();
        courseOrder.setCreateTime(now);
        courseOrder.setDiscountAmount(new BigDecimal(NumberConstants.ZERO));
        courseOrder.setOrderNo(orderSn);
        courseOrder.setOrderType(NumberConstants.ONE);
        courseOrder.setPayType(dto.getPayType());
        courseOrder.setTotalAmount(order2RedisDto.getAmount());
        courseOrder.setPayAmount(courseOrder.getTotalAmount().subtract(courseOrder.getDiscountAmount()));
        courseOrder.setStatusOrder(CourseOrder.STATE_WAIT_PAY);
        courseOrder.setTitle(TigerccConstants.KILL_ORDER + order2RedisDto.getCourseName());
        courseOrder.setTotalCount(order2RedisDto.getCount());
        courseOrder.setUserId(loginId);
        courseOrder.getItems().add(courseOrderItem);
        // 保存订单
        Map<String, Object> extParams = new HashMap<>();
        extParams.put("loginId", loginId);
        extParams.put("courseIds", Collections.singletonList(order2RedisDto.getCourseId()));
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(
                MQConstants.MQ_COURSEORDER_PAY_GROUP_TRANSACTION, MQConstants.TOPIC_PAYORDER_TAGS_PAYORDER
                , MessageBuilder.withPayload(JSON.toJSONString(new PayOrder2MQDto(courseOrder.getPayAmount()
                                , courseOrder.getPayType(), orderSn, loginId, JSON.toJSONString(extParams)
                                , courseOrder.getTitle())))
                        .build(), courseOrder);
        // 断言下单状态
        AssertUtils.isEquals(transactionSendResult.getLocalTransactionState()
                , LocalTransactionState.COMMIT_MESSAGE, GlobalErrorCode.ORDER_MISS);
        // 断言发送事务消息状态
        AssertUtils.isEquals(transactionSendResult.getSendStatus()
                , SendStatus.SEND_OK, GlobalErrorCode.SERVICE_TRANSACTION_MESSAGE_FAILED);
        // 删除token，防止重复提交
        redisTemplate.delete(token);
        return JsonResult.success(orderSn);
    }
}
