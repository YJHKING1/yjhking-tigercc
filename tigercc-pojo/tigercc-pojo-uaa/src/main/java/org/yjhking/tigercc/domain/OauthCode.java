package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 授权码
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@TableName("oauth_code")
public class OauthCode extends Model<OauthCode> {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 授权码(未加密)
     */
    private String code;
    /**
     * AuthorizationRequestHolder.java对象序列化后的二进制数据
     */
    private byte[] authentication;
    
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public byte[] getAuthentication() {
        return authentication;
    }
    
    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }
    
    @Override
    protected Serializable pkVal() {
        return null;
    }
    
    @Override
    public String toString() {
        return "OauthCode{" +
                ", code=" + code +
                ", authentication=" + authentication +
                "}";
    }
}
