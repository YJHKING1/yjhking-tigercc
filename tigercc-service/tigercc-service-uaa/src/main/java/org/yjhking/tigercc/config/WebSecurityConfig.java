package org.yjhking.tigercc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.yjhking.tigercc.mapper.PermissionMapper;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled= true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private PermissionMapper permissionMapper;
    
    //提供用户信息，这里没有从数据库查询用户信息，在内存中模拟
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager =
                new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("zs").password("123").authorities("admin").build());
        return inMemoryUserDetailsManager;
    }
    
    //密码编码器：不加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //授权规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // web授权
        permissionMapper.selectList(null).forEach(permission -> {
            try {
                http.authorizeRequests().antMatchers(permission.getResource()).hasAuthority(permission.getSn());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()  //登录路径放行
                .antMatchers("/login.html").permitAll() //对登录页面跳转路径放行
                .anyRequest().authenticated() //其他路径都要拦截
                .and().formLogin()  //允许表单登录， 设置登陆页
                .successForwardUrl("/login/loginSuccess") // 设置登陆成功页
                .loginPage("/login.html")   //登录页面跳转地址
                .loginProcessingUrl("/login")   //登录处理地址(必须)
                .and().logout().permitAll()    //登出
                .and().csrf().disable();       //关闭跨域伪造检查
    }
    
    //配置认证管理器，授权模式为“poassword”时会用到
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}