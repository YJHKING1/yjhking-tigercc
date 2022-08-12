package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.Role;
import org.yjhking.tigercc.mapper.RoleMapper;
import org.yjhking.tigercc.service.IRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
