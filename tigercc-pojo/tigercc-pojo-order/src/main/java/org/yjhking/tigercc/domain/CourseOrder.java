package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@TableName("t_course_order")
@Data
public class CourseOrder extends Model<CourseOrder> {
    
    private static final long serialVersionUID = 1L;
    /**
     * 下单成功待支付
     */
    public static final int STATE_WAIT_PAY = 0;
    /**
     * 支付成功，订单完成
     */
    public static final int STATE_SUCCESS_PAY = 1;
    /**
     * 手动取消订单
     */
    public static final int STATE_USER_CANCEL = 2;
    /**
     * 超时自动取消订单
     */
    public static final int STATE_AUTO_CANCEL = 3;
    public static final String ORDER_NO = "order_no";
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
    @TableField(exist = false)
    private List<CourseOrderItem> items = new ArrayList<>();
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
