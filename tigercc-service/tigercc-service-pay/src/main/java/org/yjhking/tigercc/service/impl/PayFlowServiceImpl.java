package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.PayFlow;
import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.mapper.PayFlowMapper;
import org.yjhking.tigercc.service.IPayFlowService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class PayFlowServiceImpl extends ServiceImpl<PayFlowMapper, PayFlow> implements IPayFlowService {
    
    @Override
    public PayFlow selectByOutTradeNo(String out_trade_no) {
        return selectOne(new EntityWrapper<PayFlow>().eq(PayFlow.OUT_TRADE_NO, out_trade_no));
    }
    
    @Override
    public void create(AlipayNotifyDto alipayNotifyDto) {
        PayFlow payFlow = new PayFlow();
        try {
            payFlow.setNotifyTime(new SimpleDateFormat().parse(alipayNotifyDto.getNotify_time()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        payFlow.setSubject(alipayNotifyDto.getSubject());
        payFlow.setOutTradeNo(alipayNotifyDto.getOut_trade_no());
        payFlow.setTotalAmount(new BigDecimal(alipayNotifyDto.getTotal_amount()));
        payFlow.setTradeStatus(alipayNotifyDto.getTrade_status());
        payFlow.setCode(alipayNotifyDto.getCode());
        payFlow.setMsg(alipayNotifyDto.getMsg());
        payFlow.setPassbackParams(alipayNotifyDto.getPassback_params());
        payFlow.setPaySuccess(true);
        payFlow.setResultDesc("");
        insert(payFlow);
    }
}
