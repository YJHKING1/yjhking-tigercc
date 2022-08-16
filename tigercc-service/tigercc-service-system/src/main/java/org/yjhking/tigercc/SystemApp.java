package org.yjhking.tigercc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// 注册到Nacos
@EnableDiscoveryClient
@EnableFeignClients
public class SystemApp {
    public static void main(String[] args) {
        SpringApplication.run(SystemApp.class, args);
    }
}