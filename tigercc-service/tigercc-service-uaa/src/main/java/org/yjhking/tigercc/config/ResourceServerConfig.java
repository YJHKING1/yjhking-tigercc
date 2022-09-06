package org.yjhking.tigercc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//ResourceServerConfigurer:资源服务器配置
//@EnableResourceServer:开启资源服务配置
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig implements ResourceServerConfigurer {

    //2.3.配置Token的存储方案
    //基于内存的Token存储
    @Bean
    public TokenStore tokenStore(){
        //return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //2.4.配置令牌转换器 ，设置JWT签名密钥。它可以是简单的MAC密钥，也可以是RSA密钥
    private final String sign_key  = "123";

    //JWT令牌校验工具
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //设置JWT签名密钥。它可以是简单的MAC密钥，也可以是RSA密钥
        jwtAccessTokenConverter.setSigningKey(sign_key);
        return jwtAccessTokenConverter;
    }

    //授权服务安全配置
    @Override
    public void configure(ResourceServerSecurityConfigurer configurer) throws Exception {
        //微服务的资源ID
        configurer.resourceId("systemId");
        //token的存储
        configurer.tokenStore(tokenStore());
    }

    //资源的授权规则配置
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers(
                        "/swagger-ui.html","/webjars/**","/swagger-resources/**","/v2/api-docs").permitAll()
                //校验scope必须为hrm ， 对应认证服务的客户端详情配置的clientId
                .antMatchers("/**").access("#oauth2.hasScope('tigercc')")
                .anyRequest().authenticated()
                .and().csrf().disable();

    }
}
