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
        // ?????????????????????
        AlipayInfo alipayInfo = alipayInfoService.selectList(null).get(NumberConstants.ZERO);
        // ?????????????????????
        Factory.setOptions(getOptions(alipayInfo));
        // ???????????????????????????
        AlipayTradePagePayResponse response = null;
        try {
            // ??????url
            String returnUrl = VerifyUtils.hasLength(dto.getCallUrl()) ? dto.getCallUrl() : alipayInfo.getReturnUrl();
            response = Factory.Payment.Page().pay(payOrder.getSubject(), payOrder.getOrderNo()
                    , payOrder.getAmount().toString(), returnUrl);
            // ????????????????????????
            AssertUtils.isTrue(ResponseChecker.success(response), GlobalErrorCode.PAY_IS_ERROR);
            payOrder.setPayStatus(PayOrder.STATUS_PAYMENTS);
            payOrder.setUpdateTime(new Date());
            payOrderService.updateById(payOrder);
            // ??????html
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
        // ??????
        Map<String, String> paramsMap = JSON.parseObject(JSON.toJSONString(dto), Map.class);
        boolean signVerified = false;
        try {
            signVerified = Factory.Payment.Common().verifyNotify(paramsMap);
            AssertUtils.isTrue(signVerified, GlobalErrorCode.PAY_SIGN_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalCustomException(GlobalErrorCode.PAY_SIGN_FAILED);
        }
        // ?????????
        AssertUtils.isFalse(dto.isTradeWit(), GlobalErrorCode.PAY_WAIT);
        // ?????????????????????
        PayOrder payOrder = payOrderService.selectByOrderNo(out_trade_no);
        AssertUtils.isNotNull(payOrder, GlobalErrorCode.ORDER_NUM_ERROR);
        AssertUtils.isEquals(payOrder.getPayStatus(), PayOrder.STATUS_PAYMENTS, GlobalErrorCode.ORDER_PROCESSED);
        // ????????????
        BigDecimal totalAmount = new BigDecimal(total_amount);
        AssertUtils.isEquals(payOrder.getAmount().compareTo(totalAmount), NumberConstants.ZERO
                , GlobalErrorCode.PAY_MONEY_ERROR);
        // ????????????
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
        // ????????????
        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(dto)).build();
        SendResult mqSend2CourseResult = rocketMQTemplate.sendMessageInTransaction(MQConstants.TX_PAY_RESULT_GROUP
                , MQConstants.TOPIC_PAY_TAGS_PAY, message, null);
        // ????????????
        SendResult result = rocketMQTemplate.syncSend(
                MQConstants.TOPIC_COURSE_ORDER_DEALY + RedisConstants.REDIS_VERIFY
                        + MQConstants.TAGS_COURSE_ORDER_DEALY,
                MessageBuilder.withPayload(JSON.toJSONString(new PlaceCourseOrderTo(out_trade_no))).build()
                , 2000, 5);
        // ????????????????????????
        AssertUtils.isEquals(mqSend2CourseResult.getSendStatus(), SendStatus.SEND_OK, GlobalErrorCode.PAY_IS_ERROR);
        return TigerccConstants.ALIPAY_SUCCESS;
    }
    
    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    private static Config getOptions(AlipayInfo alipayInfo) {
        Config config = new Config();
        //??????
        config.protocol = alipayInfo.getProtocol();
        //??????
        config.gatewayHost = alipayInfo.getGatewayHost();
        //????????????
        config.signType = alipayInfo.getSignType();
        //??????ID
        config.appId = alipayInfo.getAppId();
        // ???????????? ?????????
        config.merchantPrivateKey = alipayInfo.getMerchantPrivateKey();
        // ??????????????? ?????????
        config.alipayPublicKey = alipayInfo.getAlipayPublicKey();
        //???????????????????????????????????????????????????
        config.notifyUrl = alipayInfo.getNotifyUrl();
        return config;
    }
}
