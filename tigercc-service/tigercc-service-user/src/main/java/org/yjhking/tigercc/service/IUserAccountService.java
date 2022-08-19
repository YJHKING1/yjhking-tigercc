package org.yjhking.tigercc.service;

import com.baomidou.mybatisplus.service.IService;
import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.domain.UserAccount;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
public interface IUserAccountService extends IService<UserAccount> {
    
    void save(User user);
}
