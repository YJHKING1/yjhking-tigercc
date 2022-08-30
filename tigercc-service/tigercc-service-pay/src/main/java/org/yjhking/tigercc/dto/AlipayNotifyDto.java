package org.yjhking.tigercc.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Data
public class AlipayNotifyDto {
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    public static final String TRADE_CLOSED = "TRADE_CLOSED";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
    
    private String charset;
    private String gmt_create;
    private String gmt_payment;
    private String notify_time;
    private String subject;
    private String sign;
    private String buyer_id;
    private String invoice_amount;
    private String version;
    private String notify_id;
    private String fund_bill_list;
    private String notify_type;
    private String out_trade_no;
    private String total_amount;
    //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、	TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
    private String trade_status;
    private String trade_no;
    private String auth_app_id;
    private String receipt_amount;
    private String point_amount;
    private String app_id;
    private String buyer_pay_amount;
    private String sign_type;
    private String seller_id;
    private String code;
    private String msg;
    private String passback_params;
    
    @JSONField(serialize = false)
    public boolean isTradeSuccess() {
        return this.trade_status.equals(TRADE_SUCCESS) || this.trade_status.equals(TRADE_FINISHED);
    }
    
    @JSONField(serialize = false)
    public boolean isTradeWit() {
        return this.trade_status.equals(WAIT_BUYER_PAY);
    }
    
    @JSONField(serialize = false)
    public boolean isTradeFail() {
        //未付款交易超时关闭，或支付完成后全额退款。
        return this.trade_status.equals(TRADE_CLOSED);
    }
    
    public Map<String, Object> passbackParams2Map() {
        if (!StringUtils.hasLength(passback_params)) return null;
        
        Map<String, Object> map = new HashMap<>();
        
        try {
            passback_params = URLDecoder.decode(passback_params, "UTF-8");
            
            if (passback_params.indexOf("&") < 0) {
                String[] params = passback_params.split("=");
                map.put(params[0], params[1]);
            } else {
                String[] params = passback_params.split("&");
                for (String param : params) {
                    String[] paramKeyValue = param.split("=");
                    map.put(paramKeyValue[0], paramKeyValue[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}