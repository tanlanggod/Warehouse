package com.warehouse.dto;

import lombok.Data;

/**
 * 登录成功返回结构，包含 Token 与用户信息。
 */
@Data
public class LoginResponse {
    private String token;
    private Integer userId;
    private String username;
    private String role;
    private String realName;
}

