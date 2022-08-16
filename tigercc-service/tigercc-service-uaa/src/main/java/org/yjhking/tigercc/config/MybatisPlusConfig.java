package org.yjhking.tigercc.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// mapper接口扫描
@MapperScan("org.yjhking.tigercc.mapper")
// 开启事务管理，让我们在service中可以使用事务注解，springboot项目默认开启
//@EnableTransactionManagement
public class MybatisPlusConfig {
    /**
     * 分页插件配置对象，Mybatis-plus需要此配置对象
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}