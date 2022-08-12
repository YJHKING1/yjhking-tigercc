package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.Menu;
import org.yjhking.tigercc.mapper.MenuMapper;
import org.yjhking.tigercc.service.IMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
