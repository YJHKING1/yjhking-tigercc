package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.Login;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.mapper.LoginMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ILoginService;

/**
 * <p>
 * 登录表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {
    
    @Override
    public JsonResult save(RegisterDto dto) {
        Login login = new Login();
        login.setUsername(dto.getMobile());
        // todo 密码加密
        login.setPassword(dto.getPassword());
        login.setType(Login.TYPE_FRONT);
        login.setEnabled(Login.TYPE_TRUE);
        login.setAccountNonExpired(Login.TYPE_TRUE);
        login.setCredentialsNonExpired(Login.TYPE_TRUE);
        login.setAccountNonLocked(Login.TYPE_TRUE);
        insert(login);
        return JsonResult.success(login.getId());
    }
}
