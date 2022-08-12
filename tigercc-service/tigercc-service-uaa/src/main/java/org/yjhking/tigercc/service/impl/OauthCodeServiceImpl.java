package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.OauthCode;
import org.yjhking.tigercc.mapper.OauthCodeMapper;
import org.yjhking.tigercc.service.IOauthCodeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 授权码 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class OauthCodeServiceImpl extends ServiceImpl<OauthCodeMapper, OauthCode> implements IOauthCodeService {

}
