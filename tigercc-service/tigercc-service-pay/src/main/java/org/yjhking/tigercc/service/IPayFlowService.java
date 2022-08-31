package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.PayFlow;
import org.yjhking.tigercc.dto.AlipayNotifyDto;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
public interface IPayFlowService extends IService<PayFlow> {
    
    PayFlow selectByOutTradeNo(String out_trade_no);
    
    void create(AlipayNotifyDto alipayNotifyDto);
}
