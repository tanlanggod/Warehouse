package com.warehouse.repository;

import com.warehouse.entity.StockAdjustment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 库存调整记录仓储接口。
 */
@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Integer> {
    Page<StockAdjustment> findByProductId(Integer productId, Pageable pageable);
}

