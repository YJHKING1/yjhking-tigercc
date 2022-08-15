package org.yjhking.tigercc.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.yjhking.tigercc.constants.RegularConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 手机验证码DTO
 *
 * @author YJH
 */
@Data
public class MobileCodeDto {
    /**
     * 手机
     */
    @Pattern(message = "电话格式不正确！", regexp = RegularConstants.PHONE_REGULAR)
    private String mobile;
    /**
     * 验证码
     */
    @Length(min = 4, max = 4, message = "验证码长度必须为4位！")
    private String imageCode;
    /**
     * 验证码校验Key
     */
    @NotEmpty
    private String imageCodeKey;
}
