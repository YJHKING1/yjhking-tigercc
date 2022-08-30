package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.TigerccConstants;
import org.yjhking.tigercc.domain.Login;
import org.yjhking.tigercc.dto.LoginDto;
import org.yjhking.tigercc.dto.RegisterDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.LoginMapper;
import org.yjhking.tigercc.result.JsonResult;
import org.yjhking.tigercc.service.ILoginService;
import org.yjhking.tigercc.utils.AssertUtils;

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
    
    @Override
    public JsonResult common(LoginDto dto) {
        Login login = selectOne(new EntityWrapper<Login>().eq(TigerccConstants.USERNAME, dto.getUsername()));
        AssertUtils.isNotNull(login, GlobalErrorCode.USER_IS_NULL);
        AssertUtils.isEqualsTrim(login.getPassword(), dto.getPassword(), GlobalErrorCode.USER_PASSWORD_IS_ERROR);
        return JsonResult.success();
    }
}
