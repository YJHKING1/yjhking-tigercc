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
@TableName("t_course_order")
public class CourseOrder extends Model<CourseOrder> {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最后支付更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 支付总的价格
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    /**
     * 支付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;
    /**
     * 优惠金额
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;
    /**
     * 秒杀数量
     */
    @TableField("total_count")
    private Integer totalCount;
    /**
     * // 订单状态 ：
     * //0下单成功待支付，
     * //1支付成功订单完成
     * //2用户手动取消订单(未支付)
     * //3.支付失败
     * //4.超时自动订单取消
     */
    @TableField("status_order")
    private Integer statusOrder;
    /**
     * 用户
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 订单标题
     */
    private String title;
    private Integer version;
    /**
     * 支付方式:0余额直接，1支付宝，2微信,3银联
     */
    @TableField("pay_type")
    private Integer payType;
    /**
     * 0普通订单 ， 1秒杀订单
     */
    @TableField("order_type")
    private Integer orderType;
    
    
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
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPayAmount() {
        return payAmount;
    }
    
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    public Integer getStatusOrder() {
        return statusOrder;
    }
    
    public void setStatusOrder(Integer statusOrder) {
        this.statusOrder = statusOrder;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public Integer getPayType() {
        return payType;
    }
    
    public void setPayType(Integer payType) {
        this.payType = payType;
    }
    
    public Integer getOrderType() {
        return orderType;
    }
    
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "CourseOrder{" +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNo=" + orderNo +
                ", totalAmount=" + totalAmount +
                ", payAmount=" + payAmount +
                ", discountAmount=" + discountAmount +
                ", totalCount=" + totalCount +
                ", statusOrder=" + statusOrder +
                ", userId=" + userId +
                ", title=" + title +
                ", version=" + version +
                ", payType=" + payType +
                ", orderType=" + orderType +
                "}";
    }
}
