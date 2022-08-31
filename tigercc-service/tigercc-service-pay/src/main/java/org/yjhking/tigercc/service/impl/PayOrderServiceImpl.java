package org.yjhking.tigercc.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.AlipayInfo;
import org.yjhking.tigercc.domain.PayOrder;
import org.yjhking.tigercc.dto.PlaceCourseOrderTo;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
import org.yjhking.tigercc.mapper.PayOrderMapper;
import org.yjhking.tigercc.service.IAlipayInfoService;
import org.yjhking.tigercc.service.IPayOrderService;

import javax.annotation.Resource;

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
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {
    @Resource
    private IAlipayInfoService alipayInfoService;
    
    @Override
    public PayOrder selectByOrderNo(String orderNo) {
        return selectOne(new EntityWrapper<PayOrder>().eq(PayOrder.ORDER_NO, orderNo));
    }
    
    @Override
    public void closeOrder(PlaceCourseOrderTo placeCourseOrderTo) {
        log.info("支付关单申请...");
        //查询ali支付配置参数
        AlipayInfo alipayInfo = alipayInfoService.selectList(null).get(0);
        //配置对象
        Config config = getOptions(alipayInfo);
        Factory.setOptions(config);
        try {
            // 2. 发起API调用
            AlipayTradeCloseResponse response = Factory.Payment.Common().close(placeCourseOrderTo.getOrderNo());
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
    
    private static Config getOptions(AlipayInfo alipayInfo) {
        Config config = new Config();
        //协议
        config.protocol = alipayInfo.getProtocol();
        //网关
        config.gatewayHost = alipayInfo.getGatewayHost();
        //签名算法
        config.signType = alipayInfo.getSignType();
        //应用ID
        config.appId = alipayInfo.getAppId();
        // 应用私钥 ：加签
        config.merchantPrivateKey = alipayInfo.getMerchantPrivateKey();
        // 支付宝公钥 ：验签
        config.alipayPublicKey = alipayInfo.getAlipayPublicKey();
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayInfo.getNotifyUrl();
        return config;
    }
}
