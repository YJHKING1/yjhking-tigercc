package org.yjhking.tigercc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// 注册到Nacos
@EnableDiscoveryClient
public class UaaApp {
    public static void main(String[] args) {
        SpringApplication.run(UaaApp.class, args);
    }
}