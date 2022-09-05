package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.Permission;
import org.yjhking.tigercc.mapper.PermissionMapper;
import org.yjhking.tigercc.service.IPermissionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Resource
    private PermissionMapper permissionMapper;
    
    @Override
    public List<Permission> selectByLoginId(Long id) {
        return permissionMapper.selectByLoginId(id);
    }
}
