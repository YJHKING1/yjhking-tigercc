package org.yjhking.tigercc.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author YJH
 */
@Data
public class LoginDto {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotNull(message = "登录类型不能为空")
    private Integer Type;
}
