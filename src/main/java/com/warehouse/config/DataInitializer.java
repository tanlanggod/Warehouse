package com.warehouse.config;

import com.warehouse.entity.User;
import com.warehouse.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 启动数据初始化逻辑，负责初始化管理员账号并修复明文密码。
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(User.UserRole.ADMIN);
                admin.setRealName("系统管理员");
                userRepository.save(admin);
            } else {
                for (User u : users) {
                    String pwd = u.getPassword();
                    // 简单判断：不是以 BCrypt 前缀开头的视为未加密
                    if (pwd == null || !(pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$"))) {
                        u.setPassword(passwordEncoder.encode(pwd == null ? "admin123" : pwd));
                        userRepository.save(u);
                    }
                }
            }
        };
    }
}


