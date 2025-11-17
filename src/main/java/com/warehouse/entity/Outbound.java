package com.warehouse.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 出库记录实体，描述商品出库及客户信息。
 */
@Entity
@Table(name = "outbound")
@Data
public class Outbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "outbound_no", nullable = false, unique = true, length = 50)
    private String outboundNo;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "outbound_date", nullable = false)
    private LocalDateTime outboundDate;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;

    @Column(length = 255)
    private String remark;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (outboundDate == null) {
            outboundDate = LocalDateTime.now();
        }
    }
}

