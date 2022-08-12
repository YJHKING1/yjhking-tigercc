package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.OauthClientDetails;
import org.yjhking.tigercc.mapper.OauthClientDetailsMapper;
import org.yjhking.tigercc.service.IOauthClientDetailsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * oauth2客户端详情配置 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {

}
