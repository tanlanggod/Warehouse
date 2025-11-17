package com.warehouse.service;

import com.warehouse.common.PageResult;
import com.warehouse.entity.Product;
import com.warehouse.entity.StockAdjustment;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.StockAdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存调整服务，处理增减库存及历史记录查询。
 */
@Service
public class StockAdjustmentService {
    @Autowired
    private StockAdjustmentRepository stockAdjustmentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public StockAdjustment createAdjustment(StockAdjustment adjustment) {
        Product product = productRepository.findById(adjustment.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        int beforeQty = product.getStockQty();
        int afterQty;

        if (adjustment.getAdjustmentType() == StockAdjustment.AdjustmentType.INCREASE) {
            afterQty = beforeQty + adjustment.getQuantity();
        } else {
            afterQty = beforeQty - adjustment.getQuantity();
            if (afterQty < 0) {
                throw new RuntimeException("调整后库存不能为负数");
            }
        }

        adjustment.setBeforeQty(beforeQty);
        adjustment.setAfterQty(afterQty);

        // 更新商品库存
        product.setStockQty(afterQty);
        productRepository.save(product);

        return stockAdjustmentRepository.save(adjustment);
    }

    public PageResult<StockAdjustment> getAdjustments(Integer productId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<StockAdjustment> adjustmentPage;
        if (productId != null) {
            adjustmentPage = stockAdjustmentRepository.findByProductId(productId, pageable);
        } else {
            adjustmentPage = stockAdjustmentRepository.findAll(pageable);
        }
        return new PageResult<>(adjustmentPage.getTotalElements(), adjustmentPage.getContent());
    }
}

