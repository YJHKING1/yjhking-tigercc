package org.yjhking.tigercc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.yjhking.tigercc.domain.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    
    List<Permission> selectByLoginId(Long id);
}
