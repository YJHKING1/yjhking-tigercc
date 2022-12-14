package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.result.JsonResult;

/**
 * <p>
 * 会员登录账号 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
public interface IUserService extends IService<User> {
    
    JsonResult register(RegisterDto dto);
}
