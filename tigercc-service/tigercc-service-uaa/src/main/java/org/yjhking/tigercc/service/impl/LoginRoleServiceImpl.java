package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.LoginRole;
import org.yjhking.tigercc.mapper.LoginRoleMapper;
import org.yjhking.tigercc.service.ILoginRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色中间表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class LoginRoleServiceImpl extends ServiceImpl<LoginRoleMapper, LoginRole> implements ILoginRoleService {

}
