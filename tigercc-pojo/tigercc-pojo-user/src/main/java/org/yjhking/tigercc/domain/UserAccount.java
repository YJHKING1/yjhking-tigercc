package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@TableName("t_user_account")
public class UserAccount extends Model<UserAccount> {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    @TableField("usable_amount")
    private BigDecimal usableAmount;
    @TableField("frozen_amount")
    private BigDecimal frozenAmount;
    @TableField("create_time")
    private Long createTime;
    @TableField("update_time")
    private Long updateTime;
    /**
     * 支付密码
     */
    private String password;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getUsableAmount() {
        return usableAmount;
    }
    
    public void setUsableAmount(BigDecimal usableAmount) {
        this.usableAmount = usableAmount;
    }
    
    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }
    
    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }
    
    public Long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "UserAccount{" +
                ", id=" + id +
                ", usableAmount=" + usableAmount +
                ", frozenAmount=" + frozenAmount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", password=" + password +
                "}";
    }
}
