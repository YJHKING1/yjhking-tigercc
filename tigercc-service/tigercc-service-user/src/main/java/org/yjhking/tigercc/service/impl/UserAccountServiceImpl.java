package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.domain.UserAccount;
import org.yjhking.tigercc.mapper.UserAccountMapper;
import org.yjhking.tigercc.service.IUserAccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {
    
    @Override
    public void save(User user) {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(user.getId());
        userAccount.setUsableAmount(BigDecimal.valueOf(0));
        userAccount.setFrozenAmount(BigDecimal.valueOf(0));
        userAccount.setCreateTime(new Date().getTime());
        insert(userAccount);
    }
}
