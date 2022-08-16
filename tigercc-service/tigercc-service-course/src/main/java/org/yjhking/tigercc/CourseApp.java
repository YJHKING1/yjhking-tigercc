package org.yjhking.tigercc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// 注册到Nacos
@EnableDiscoveryClient
public class CourseApp {
    public static void main(String[] args) {
        SpringApplication.run(CourseApp.class, args);
    }
}