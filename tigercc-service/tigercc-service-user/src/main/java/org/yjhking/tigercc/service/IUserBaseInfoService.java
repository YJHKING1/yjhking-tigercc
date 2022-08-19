package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.domain.UserBaseInfo;

/**
 * <p>
 * 会员基本信息 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
public interface IUserBaseInfoService extends IService<UserBaseInfo> {
    
    void save(User user);
}
