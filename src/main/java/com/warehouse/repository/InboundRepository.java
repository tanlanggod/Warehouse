package com.warehouse.repository;

import com.warehouse.entity.Inbound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

/**
 * 入库记录仓储接口，支持多条件分页查询。
 */
@Repository
public interface InboundRepository extends JpaRepository<Inbound, Integer> {
    @Query("SELECT i FROM Inbound i WHERE " +
           "(:startDate IS NULL OR i.inboundDate >= :startDate) AND " +
           "(:endDate IS NULL OR i.inboundDate <= :endDate) AND " +
           "(:productId IS NULL OR i.product.id = :productId) AND " +
           "(:supplierId IS NULL OR i.supplier.id = :supplierId)")
    Page<Inbound> findByConditions(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    @Param("productId") Integer productId,
                                    @Param("supplierId") Integer supplierId,
                                    Pageable pageable);

    java.util.List<Inbound> findByInboundDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

