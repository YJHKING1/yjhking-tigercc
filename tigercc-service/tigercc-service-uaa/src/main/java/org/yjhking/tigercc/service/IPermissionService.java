package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
public interface IPermissionService extends IService<Permission> {
    
    List<Permission> selectByLoginId(Long id);
}
