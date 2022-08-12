package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.mapper.UserMapper;
import org.yjhking.tigercc.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
