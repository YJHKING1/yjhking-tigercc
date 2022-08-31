package org.yjhking.tigercc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.MQConstants;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.constants.RedisConstants;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.AlipayInfo;
import org.yjhking.tigercc.domain.PayFlow;
import org.yjhking.tigercc.domain.PayOrder;
import org.yjhking.tigercc.dto.AlipayNotifyDto;
import org.yjhking.tigercc.dto.PayParamDto;
import org.yjhking.tigercc.dto.PlaceCourseOrderTo;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.exception.GlobalCustomException;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.IAlipayInfoService;
import org.yjhking.tigercc.service.IPayOrderService;
import org.yjhking.tigercc.service.PayService;
import org.yjhking.tigercc.utils.AssertUtils;
import org.yjhking.tigercc.utils.VerifyUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author YJH
 */
@Service
public class PayServiceImpl implements PayService {
    @Resource
    private IPayOrderService payOrderService;
    @Resource
    private IAlipayInfoService alipayInfoService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    
    @Override
    public JsonResult apply(PayParamDto dto) {
        PayOrder payOrder = payOrderService.selectByOrderNo(dto.getOrderNo());
        AssertUtils.isNotNull(payOrder, GlobalErrorCode.SERVICE_ILLEGAL_REQUEST);
        AssertUtils.isEquals(payOrder.getPayStatus(), PayOrder.STATUS_WAIT_PAY, GlobalErrorCode.ORDER_TYPE_ERROR);
        // 读取支付宝配置
        AlipayInfo alipayInfo = alipayInfoService.selectList(null).get(NumberConstants.ZERO);
        // 设置支付宝配置
        Factory.setOptions(getOptions(alipayInfo));
        // 创建支付宝支付请求
        AlipayTradePagePayResponse response = null;
        try {
            // 回调url
            String returnUrl = VerifyUtils.hasLength(dto.getCallUrl()) ? dto.getCallUrl() : alipayInfo.getReturnUrl();
            response = Factory.Payment.Page().pay(payOrder.getSubject(), payOrder.getOrderNo()
                    , payOrder.getAmount().toString(), returnUrl);
            // 校验支付是否成功
            AssertUtils.isTrue(ResponseChecker.success(response), GlobalErrorCode.PAY_IS_ERROR);
            payOrder.setPayStatus(PayOrder.STATUS_PAYMENTS);
            payOrder.setUpdateTime(new Date());
            payOrderService.updateById(payOrder);
            // 返回html
            return JsonResult.success(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(GlobalErrorCode.PAY_IS_ERROR);
        }
    }
    
    @Override
    public String alipayNotify(AlipayNotifyDto dto) {
        String out_trade_no = dto.getOut_trade_no();
        String total_amount = dto.getTotal_amount();
        String trade_status = dto.getTrade_status();
        AssertUtils.isNotNull(out_trade_no, GlobalErrorCode.ORDER_NUM_ERROR);
        // 验签
        Map<String, String> paramsMap = JSON.parseObject(JSON.toJSONString(dto), Map.class);
        boolean signVerified = false;
        try {
            signVerified = Factory.Payment.Common().verifyNotify(paramsMap);
            AssertUtils.isTrue(signVerified, GlobalErrorCode.PAY_SIGN_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalCustomException(GlobalErrorCode.PAY_SIGN_FAILED);
        }
        // 待支付
        AssertUtils.isFalse(dto.isTradeWit(), GlobalErrorCode.PAY_WAIT);
        // 判断是否被处理
        PayOrder payOrder = payOrderService.selectByOrderNo(out_trade_no);
        AssertUtils.isNotNull(payOrder, GlobalErrorCode.ORDER_NUM_ERROR);
        AssertUtils.isEquals(payOrder.getPayStatus(), PayOrder.STATUS_PAYMENTS, GlobalErrorCode.ORDER_PROCESSED);
        // 核对金额
        BigDecimal totalAmount = new BigDecimal(total_amount);
        AssertUtils.isEquals(payOrder.getAmount().compareTo(totalAmount), NumberConstants.ZERO
                , GlobalErrorCode.PAY_MONEY_ERROR);
        // 记录流水
        payOrder.setPayStatus(dto.isTradeSuccess() ? PayOrder.STATUS_PAY_SUCCESS : PayOrder.STATUS_AUTO_CANCEL);
        Date date = new Date();
        payOrder.setUpdateTime(date);
        // payOrderService.updateById(payOrder);
        PayFlow payFlow = new PayFlow();
        payFlow.setCode(dto.getCode());
        payFlow.setMsg(dto.getMsg());
        payFlow.setNotifyTime(date);
        payFlow.setOutTradeNo(out_trade_no);
        payFlow.setPaySuccess(dto.isTradeSuccess());
        payFlow.setSubject(payOrder.getSubject());
        payFlow.setTotalAmount(totalAmount);
        payFlow.setTradeStatus(dto.getTrade_status());
        // insert(payFlow);
        // 发送消息
        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(dto)).build();
        SendResult mqSend2CourseResult = rocketMQTemplate.sendMessageInTransaction(MQConstants.TX_PAY_RESULT_GROUP
                , MQConstants.TOPIC_PAY_TAGS_PAY, message, null);
        // 延迟关单
        SendResult result = rocketMQTemplate.syncSend(
                MQConstants.TOPIC_COURSE_ORDER_DEALY + RedisConstants.REDIS_VERIFY
                        + MQConstants.TAGS_COURSE_ORDER_DEALY,
                MessageBuilder.withPayload(JSON.toJSONString(new PlaceCourseOrderTo(out_trade_no))).build()
                , 2000, 5);
        // 校验是否发送成功
        AssertUtils.isEquals(mqSend2CourseResult.getSendStatus(), SendStatus.SEND_OK, GlobalErrorCode.PAY_IS_ERROR);
        return TigerccConstants.ALIPAY_SUCCESS;
    }
    
    /**
     * 获取支付宝配置
     *
     * @return 支付宝配置
     */
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
