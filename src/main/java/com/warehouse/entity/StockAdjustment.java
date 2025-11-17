package com.warehouse.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 库存调整记录，追踪手工增减库存的原因与数量。
 */
@Entity
@Table(name = "stock_adjustment")
@Data
public class StockAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "adjustment_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AdjustmentType adjustmentType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "before_qty", nullable = false)
    private Integer beforeQty;

    @Column(name = "after_qty", nullable = false)
    private Integer afterQty;

    @Column(nullable = false, length = 255)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum AdjustmentType {
        INCREASE,  // 增加
        DECREASE   // 减少
    }
}

