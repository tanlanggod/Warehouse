package com.warehouse.repository;

import com.warehouse.entity.Outbound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

/**
 * 出库记录仓储接口，支持多条件分页查询。
 */
@Repository
public interface OutboundRepository extends JpaRepository<Outbound, Integer> {
    @Query("SELECT o FROM Outbound o WHERE " +
           "(:startDate IS NULL OR o.outboundDate >= :startDate) AND " +
           "(:endDate IS NULL OR o.outboundDate <= :endDate) AND " +
           "(:productId IS NULL OR o.product.id = :productId) AND " +
           "(:customerId IS NULL OR o.customer.id = :customerId)")
    Page<Outbound> findByConditions(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     @Param("productId") Integer productId,
                                     @Param("customerId") Integer customerId,
                                     Pageable pageable);

    java.util.List<Outbound> findByOutboundDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

