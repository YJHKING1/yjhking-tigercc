package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.Config;
import org.yjhking.tigercc.mapper.ConfigMapper;
import org.yjhking.tigercc.service.IConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
