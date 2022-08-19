package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@TableName("t_pay_flow")
public class PayFlow extends Model<PayFlow> {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 交易时间
     */
    @TableField("notify_time")
    private Date notifyTime;
    /**
     * 标题
     */
    private String subject;
    /**
     * 交易号，对应订单号
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    @TableField("trade_status")
    private String tradeStatus;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;
    @TableField("passback_params")
    private String passbackParams;
    @TableField("pay_success")
    private Boolean paySuccess;
    @TableField("result_desc")
    private String resultDesc;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getNotifyTime() {
        return notifyTime;
    }
    
    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getOutTradeNo() {
        return outTradeNo;
    }
    
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getTradeStatus() {
        return tradeStatus;
    }
    
    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getPassbackParams() {
        return passbackParams;
    }
    
    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
    }
    
    public Boolean getPaySuccess() {
        return paySuccess;
    }
    
    public void setPaySuccess(Boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
    
    public String getResultDesc() {
        return resultDesc;
    }
    
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "PayFlow{" +
                ", id=" + id +
                ", notifyTime=" + notifyTime +
                ", subject=" + subject +
                ", outTradeNo=" + outTradeNo +
                ", totalAmount=" + totalAmount +
                ", tradeStatus=" + tradeStatus +
                ", code=" + code +
                ", msg=" + msg +
                ", passbackParams=" + passbackParams +
                ", paySuccess=" + paySuccess +
                ", resultDesc=" + resultDesc +
                "}";
    }
}
