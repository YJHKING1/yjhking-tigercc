package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.Login;
import org.yjhking.tigercc.mapper.LoginMapper;
import org.yjhking.tigercc.service.ILoginService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
