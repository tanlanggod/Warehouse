package com.warehouse.repository;

import com.warehouse.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 客户仓储接口。
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

