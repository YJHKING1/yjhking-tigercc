package org.yjhking.tigercc.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.yjhking.tigercc.constants.RegularConstants;

import javax.validation.constraints.Pattern;

/**
 * @author YJH
 */
@Data
public class RegisterDto {
    /**
     * 图片验证码
     */
    @Length(min = 4, max = 4, message = "图片验证码长度必须为4位！")
    public String imageCode;
    /**
     * 手机
     */
    @Pattern(regexp = RegularConstants.PHONE_REGULAR, message = "电话格式不正确！")
    public String mobile;
    /**
     * 密码
     */
    @Length(min = 6, max = 16, message = "密码长度必须为6-16位！")
    public String password;
    /**
     * 校验码
     */
    @Pattern(regexp = RegularConstants.CHANNEL_REGULAR, message = "非法请求！")
    public String regChannel;
    /**
     * 短信验证码
     */
    @Length(min = 6, max = 6, message = "短信验证码长度必须为6位！")
    public String smsCode;
}
