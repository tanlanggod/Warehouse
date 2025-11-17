package com.warehouse.service;

import com.warehouse.common.PageResult;
import com.warehouse.entity.Outbound;
import com.warehouse.entity.Product;
import com.warehouse.repository.OutboundRepository;
import com.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 出库业务服务，负责出库单管理与库存扣减。
 */
@Service
public class OutboundService {
    @Autowired
    private OutboundRepository outboundRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Outbound createOutbound(Outbound outbound) {
        Product product = productRepository.findById(outbound.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStockQty() < outbound.getQuantity()) {
            throw new RuntimeException("库存不足");
        }

        // 生成出库单号
        String outboundNo = "OUT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + 
                           UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        outbound.setOutboundNo(outboundNo);

        // 更新商品库存
        product.setStockQty(product.getStockQty() - outbound.getQuantity());
        productRepository.save(product);

        return outboundRepository.save(outbound);
    }

    public PageResult<Outbound> getOutbounds(LocalDateTime startDate, LocalDateTime endDate,
                                             Integer productId, Integer customerId,
                                             Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Outbound> outboundPage = outboundRepository.findByConditions(
                startDate, endDate, productId, customerId, pageable);
        return new PageResult<>(outboundPage.getTotalElements(), outboundPage.getContent());
    }

    public Outbound getOutboundById(Integer id) {
        return outboundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("出库单不存在"));
    }

    @Transactional
    public void deleteOutbound(Integer id) {
        Outbound outbound = outboundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("出库单不存在"));

        // 恢复库存
        Product product = outbound.getProduct();
        product.setStockQty(product.getStockQty() + outbound.getQuantity());
        productRepository.save(product);

        outboundRepository.deleteById(id);
    }
}

