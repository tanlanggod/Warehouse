package com.warehouse.repository;

import com.warehouse.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 商品仓储接口，提供分页检索与库存预警查询。
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
    
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR p.name LIKE %:name%) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:status IS NULL OR p.status = :status)")
    Page<Product> findByConditions(@Param("name") String name,
                                    @Param("categoryId") Integer categoryId,
                                    @Param("status") Integer status,
                                    Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.stockQty <= p.minStock AND p.status = 1")
    List<Product> findLowStockProducts();
}

