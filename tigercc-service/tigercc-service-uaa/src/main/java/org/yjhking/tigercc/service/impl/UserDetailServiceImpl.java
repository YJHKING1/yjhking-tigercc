package org.yjhking.tigercc.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.domain.Login;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.service.ILoginService;
import org.yjhking.tigercc.service.IPermissionService;
import org.yjhking.tigercc.utils.AssertUtils;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private ILoginService loginService;
    @Resource
    private IPermissionService permissionService;
    
    /**
     * 加载数据库中的认证的用户的信息：用户名，密码，用户的权限列表
     *
     * @param username: 该方法把username传入进来，我们通过username查询用户的信息
     *                  (密码，权限列表等)然后封装成 UserDetails进行返回 ，交给security 。
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginService.selectByUsername(username);
        AssertUtils.isNotNull(login, GlobalErrorCode.UAA_USER_IS_NULL);
        return new User(login.getUsername(), login.getPassword(), login.getEnabled(), login.getAccountNonExpired()
                , login.getCredentialsNonExpired(), login.getAccountNonLocked(),
                permissionService.selectByLoginId(login.getId()).stream().map(permission ->
                        new SimpleGrantedAuthority(permission.getSn())).collect(Collectors.toList()));
    }
}