package org.yjhking.tigercc.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yjhking.tigercc.constants.RedisConstants;

@Configuration
public class RedissonConfig {
    //创建客户端
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(RedisConstants.REDIS_ADDRESS).setPassword(RedisConstants.REDIS_PASSWORD);
        return Redisson.create(config);
    }
}