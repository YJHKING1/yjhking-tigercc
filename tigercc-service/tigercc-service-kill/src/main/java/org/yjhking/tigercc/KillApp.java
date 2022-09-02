package org.yjhking.tigercc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 注册到Nacos
@EnableDiscoveryClient
@EnableScheduling
public class KillApp {
    public static void main(String[] args) {
        SpringApplication.run(KillApp.class, args);
    }
}