package org.yjhking.tigercc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

//授权服务配置
@Configuration
//开启授权服务配置
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //1.客户端详情配置(请求参数)
    @Autowired
    private DataSource dataSource ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    //1.1.注册客户端详情Bean,基于数据库,自动操作表：oauth_client_details
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        //数据库的秘钥使用了PasswordEncoder加密
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    //2.授权服务端点配置(授权码，令牌)

    @Autowired
    private AuthenticationManager authenticationManager ;

    //2.1.定义授权码服务,连接数据库 oauth_code
    @Bean
    public JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices(){
        return new JdbcAuthorizationCodeServices(dataSource);
    }
    //2.2.令牌服务配置
    //令牌的管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        //创建默认的令牌服务
        DefaultTokenServices services = new DefaultTokenServices();
        //指定客户端详情配置
        services.setClientDetailsService(jdbcClientDetailsService());
        //支持产生刷新token
        services.setSupportRefreshToken(true);
        //token存储方式
        services.setTokenStore(tokenStore());

        //设置token增强 - 设置token转换器
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter()));
        services.setTokenEnhancer(tokenEnhancerChain);  //jwtAccessTokenConverter()

        return services;
    }

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

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            //1.密码授权模式需要 密码模式，需要认证管理器
            .authenticationManager(authenticationManager)
            //2.授权码模式服务
            .authorizationCodeServices(jdbcAuthorizationCodeServices())
            //3.配置令牌管理服务
            .tokenServices(tokenService())
            //允许post方式请求
            .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //2.授权服务安全配置(URL放行)
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //对应/oauth/check_token ，路径公开
                .checkTokenAccess("permitAll()")
                //允许客户端进行表单身份验证,使用表单认证申请令牌
                .allowFormAuthenticationForClients();
    }

}
