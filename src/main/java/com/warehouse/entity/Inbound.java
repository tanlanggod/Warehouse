package com.warehouse.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 入库记录实体，描述单次入库操作及数量。
 */
@Entity
@Table(name = "inbound")
@Data
public class Inbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "inbound_no", nullable = false, unique = true, length = 50)
    private String inboundNo;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "inbound_date", nullable = false)
    private LocalDateTime inboundDate;

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
        if (inboundDate == null) {
            inboundDate = LocalDateTime.now();
        }
    }
}

