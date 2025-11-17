package com.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 系统入口类，负责启动 Spring Boot 应用并开启 JPA 审计功能。
 */
@SpringBootApplication
@EnableJpaAuditing
public class WarehouseManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseManagementApplication.class, args);
    }
}

