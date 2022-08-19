package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会员基本信息
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@TableName("t_user_base_info")
public class UserBaseInfo extends Model<UserBaseInfo> {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    @TableField("create_time")
    private Long createTime;
    @TableField("update_time")
    private Long updateTime;
    /**
     * 注册渠道
     */
    @TableField("reg_channel")
    private Integer regChannel;
    /**
     * QQ
     */
    private String qq;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 成长值
     */
    @TableField("grow_score")
    private Integer growScore;
    /**
     * 推荐人
     */
    @TableField("refer_id")
    private Long referId;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 生日
     */
    private Date birthday;
    @TableField("area_code")
    private String areaCode;
    private String address;
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Integer getRegChannel() {
        return regChannel;
    }
    
    public void setRegChannel(Integer regChannel) {
        this.regChannel = regChannel;
    }
    
    public String getQq() {
        return qq;
    }
    
    public void setQq(String qq) {
        this.qq = qq;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getGrowScore() {
        return growScore;
    }
    
    public void setGrowScore(Integer growScore) {
        this.growScore = growScore;
    }
    
    public Long getReferId() {
        return referId;
    }
    
    public void setReferId(Long referId) {
        this.referId = referId;
    }
    
    public Integer getSex() {
        return sex;
    }
    
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    
    public Date getBirthday() {
        return birthday;
    }
    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    public String getAreaCode() {
        return areaCode;
    }
    
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "UserBaseInfo{" +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", regChannel=" + regChannel +
                ", qq=" + qq +
                ", level=" + level +
                ", growScore=" + growScore +
                ", referId=" + referId +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", areaCode=" + areaCode +
                ", address=" + address +
                "}";
    }
}
