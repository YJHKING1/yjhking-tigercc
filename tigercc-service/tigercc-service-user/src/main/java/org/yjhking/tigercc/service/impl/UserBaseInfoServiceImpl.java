package org.yjhking.tigercc.service.impl;

import org.yjhking.tigercc.domain.User;
import org.yjhking.tigercc.domain.UserBaseInfo;
import org.yjhking.tigercc.mapper.UserBaseInfoMapper;
import org.yjhking.tigercc.service.IUserBaseInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 会员基本信息 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-12
 */
@Service
public class UserBaseInfoServiceImpl extends ServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements IUserBaseInfoService {
    
    @Override
    public void save(User user) {
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setId(user.getId());
        userBaseInfo.setCreateTime(new Date().getTime());
        userBaseInfo.setReferId(0L);
        userBaseInfo.setLevel(0);
        userBaseInfo.setGrowScore(0);
        insert(userBaseInfo);
    }
}
