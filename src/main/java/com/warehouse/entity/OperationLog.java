package com.warehouse.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志实体，记录系统接口调用情况
 */
@Entity
@Table(name = "operation_log")
@Data
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "request_id", length = 64, nullable = false)
    private String requestId;

    @Column(name = "user_id", nullable = true)
    private Integer userId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "user_real_name", length = 100)
    private String userRealName;

    @Column(name = "user_role", length = 50)
    private String userRole;

    @Column(name = "ip_address", length = 45, nullable = false)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "request_method", length = 10, nullable = false)
    private String requestMethod;

    @Column(name = "request_url", length = 500, nullable = false)
    private String requestUrl;

    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    @Column(name = "response_status", nullable = false)
    private Integer responseStatus;

    @Column(name = "response_time", nullable = false)
    private Long responseTime; // 响应时间，单位：毫秒

    @Column(name = "response_result", columnDefinition = "TEXT")
    private String responseResult; // 响应结果摘要

    @Column(name = "operation_type", length = 50)
    private String operationType; // 操作类型：CREATE、READ、UPDATE、DELETE、EXPORT、IMPORT

    @Column(name = "resource_type", length = 50)
    private String resourceType; // 资源类型：PRODUCT、INBOUND、OUTBOUND、USER等

    @Column(name = "resource_id", length = 50)
    private String resourceId; // 资源ID

    @Column(name = "operation_desc", length = 200)
    private String operationDesc; // 操作描述

    @Column(name = "module", length = 50)
    private String module;

    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess; // 操作是否成功

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage; // 错误信息

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 常用操作类型
    public static class OperationType {
        public static final String CREATE = "CREATE";
        public static final String READ = "READ";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String EXPORT = "EXPORT";
        public static final String IMPORT = "IMPORT";
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
        public static final String DOWNLOAD = "DOWNLOAD";
    }

    // 常用资源类型
    public static class ResourceType {
        public static final String PRODUCT = "PRODUCT";
        public static final String CATEGORY = "CATEGORY";
        public static final String SUPPLIER = "SUPPLIER";
        public static final String CUSTOMER = "CUSTOMER";
        public static final String INBOUND = "INBOUND";
        public static final String OUTBOUND = "OUTBOUND";
        public static final String USER = "USER";
        public static final String REPORT = "REPORT";
        public static final String SYSTEM = "SYSTEM";
    }
}

