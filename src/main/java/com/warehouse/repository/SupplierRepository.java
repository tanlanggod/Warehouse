package com.warehouse.repository;

import com.warehouse.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 供应商仓储接口。
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findByName(String name);
}

