package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@TableName("t_alipay_info")
public class AlipayInfo extends Model<AlipayInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户私钥
     */
    @TableField("merchant_private_key")
    private String merchantPrivateKey;
    @TableField("app_id")
    private String appId;
    /**
     * 阿里公钥
     */
    @TableField("alipay_public_key")
    private String alipayPublicKey;
    @TableField("crate_time")
    private Date crateTime;
    @TableField("update_time")
    private Date updateTime;
    private String protocol;
    @TableField("gateway_host")
    private String gatewayHost;
    @TableField("sign_type")
    private String signType;
    @TableField("notify_url")
    private String notifyUrl;
    @TableField("return_url")
    private String returnUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getGatewayHost() {
        return gatewayHost;
    }

    public void setGatewayHost(String gatewayHost) {
        this.gatewayHost = gatewayHost;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AlipayInfo{" +
        ", id=" + id +
        ", merchantPrivateKey=" + merchantPrivateKey +
        ", appId=" + appId +
        ", alipayPublicKey=" + alipayPublicKey +
        ", crateTime=" + crateTime +
        ", updateTime=" + updateTime +
        ", protocol=" + protocol +
        ", gatewayHost=" + gatewayHost +
        ", signType=" + signType +
        ", notifyUrl=" + notifyUrl +
        ", returnUrl=" + returnUrl +
        "}";
    }
}
