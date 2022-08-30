package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.PayOrder;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface IPayOrderService extends IService<PayOrder> {
    PayOrder selectByOrderNo(String orderNo);
}
