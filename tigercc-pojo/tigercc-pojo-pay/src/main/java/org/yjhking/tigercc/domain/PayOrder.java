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
@TableName("t_pay_order")
public class PayOrder extends Model<PayOrder> {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 流水创建
     */
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    /**
     * 发生金额
     */
    private BigDecimal amount;
    /**
     * 支付方式:0余额直接，1支付宝，2微信,3银联
     */
    @TableField("pay_type")
    private Integer payType;
    /**
     * 业务ID，可以关联订单ID,或者课程ID
     */
    @TableField("relation_id")
    private Long relationId;
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    @TableField("user_id")
    private Long userId;
    /**
     * 扩展参数，格式： xx=1&oo=2
     */
    @TableField("ext_params")
    private String extParams;
    /**
     * 描述
     */
    private String subject;
    /**
     * 支付状态：0待支付 ，1支付成功，2支付超时或退款订单关闭
     */
    @TableField("pay_status")
    private Integer payStatus;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Integer getPayType() {
        return payType;
    }
    
    public void setPayType(Integer payType) {
        this.payType = payType;
    }
    
    public Long getRelationId() {
        return relationId;
    }
    
    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getExtParams() {
        return extParams;
    }
    
    public void setExtParams(String extParams) {
        this.extParams = extParams;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public Integer getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "PayOrder{" +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", amount=" + amount +
                ", payType=" + payType +
                ", relationId=" + relationId +
                ", orderNo=" + orderNo +
                ", userId=" + userId +
                ", extParams=" + extParams +
                ", subject=" + subject +
                ", payStatus=" + payStatus +
                "}";
    }
}
