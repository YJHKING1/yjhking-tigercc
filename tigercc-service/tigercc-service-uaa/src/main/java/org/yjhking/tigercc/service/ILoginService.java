package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.Login;
import org.yjhking.tigercc.dto.LoginDto;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 登录表 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
public interface ILoginService extends IService<Login> {
    
    JsonResult save(RegisterDto dto);
    
    JsonResult common(LoginDto dto);
}
