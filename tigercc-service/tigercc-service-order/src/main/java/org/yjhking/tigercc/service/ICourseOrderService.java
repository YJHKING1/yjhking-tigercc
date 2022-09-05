package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.CourseOrder;
import org.yjhking.tigercc.dto.CourseKillOrderParamDto;
import org.yjhking.tigercc.dto.PlaceCourseOrderTo;
import org.yjhking.tigercc.dto.PlaceOrderDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface ICourseOrderService extends IService<CourseOrder> {
    
    String placeOrder(PlaceOrderDto dto);
    
    void saveOrderAndItem(CourseOrder courseOrder);
    
    CourseOrder selectByOrderNo(String out_trade_no);
    
    void closeOrder(PlaceCourseOrderTo placeCourseOrderTo);
    
    JsonResult killPlaceOrder(CourseKillOrderParamDto dto);
}
