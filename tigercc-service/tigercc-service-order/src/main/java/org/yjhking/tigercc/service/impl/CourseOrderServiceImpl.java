package org.yjhking.tigercc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseMarket;
import org.yjhking.tigercc.domain.CourseOrder;
import org.yjhking.tigercc.domain.CourseOrderItem;
import org.yjhking.tigercc.dto.PlaceOrderDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.feignclient.CourseFeignClient;
import org.yjhking.tigercc.mapper.CourseOrderMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ICourseOrderItemService;
import org.yjhking.tigercc.service.ICourseOrderService;
import org.yjhking.tigercc.utils.CodeGenerateUtils;
import org.yjhking.tigercc.utils.VerificationUtils;
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
    private RedisTemplate<Object,Object> redisTemplate;
    @Resource
    private CourseFeignClient courseFeignClient;
    @Resource
    private ICourseOrderItemService orderItemService;
    @Override
    public JsonResult placeOrder(PlaceOrderDto dto) {
        // todo 假数据 用户id
        Long loginId = 3L;
        // 参数校验
        String courseIdStr = StringUtils.join(dto.getCourseIds(), TigerccConstants.SEPARATOR);
        String token = (String)redisTemplate.opsForValue()
                .get(RedisConstants.TOKEN + loginId + RedisConstants.REDIS_VERIFY + courseIdStr);
        VerificationUtils.isHasLength(token, GlobalErrorCode.ORDER_REPEAT);
        VerificationUtils.isEquals(token, dto.getToken(), GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        // 获取课程数据
        JsonResult result = courseFeignClient.selectCourseDataForOrder(courseIdStr);
        VerificationUtils.isTrue(result.isSuccess(), GlobalErrorCode.COURSE_ERROR);
        VerificationUtils.isNotNull(result.getData(), GlobalErrorCode.COURSE_ERROR);
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
        insert(courseOrder);
        // 保存明细
        items.forEach(item -> item.setOrderId(courseOrder.getId()));
        orderItemService.insertBatch(items);
        return JsonResult.success();
    }
}
