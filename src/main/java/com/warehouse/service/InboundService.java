package com.warehouse.service;

import com.warehouse.common.PageResult;
import com.warehouse.entity.Inbound;
import com.warehouse.entity.Product;
import com.warehouse.repository.InboundRepository;
import com.warehouse.repository.ProductRepository;
import com.warehouse.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * 入库业务服务，负责入库单操作及库存同步。
 */
@Service
public class InboundService {
    @Autowired
    private InboundRepository inboundRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Inbound createInbound(Inbound inbound) {
        if (inbound.getProduct() == null) {
            throw new RuntimeException("商品信息不能为空");
        }
        Integer productId = inbound.getProduct().getId();
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 生成入库单号
        String inboundNo = "IN" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + 
                          UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        inbound.setInboundNo(inboundNo);

        // 更新商品库存
        product.setStockQty(product.getStockQty() + inbound.getQuantity());
        productRepository.save(product);

        return inboundRepository.save(inbound);
    }

    public PageResult<Inbound> getInbounds(LocalDateTime startDate, LocalDateTime endDate,
                                            Integer productId, Integer supplierId,
                                            Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Inbound> inboundPage = inboundRepository.findByConditions(
                startDate, endDate, productId, supplierId, pageable);
        return new PageResult<>(inboundPage.getTotalElements(), inboundPage.getContent());
    }

    public Inbound getInboundById(Integer id) {
        if (id == null) {
            throw new RuntimeException("入库单ID不能为空");
        }
        return inboundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("入库单不存在"));
    }

    @Transactional
    public void deleteInbound(Integer id) {
        if (id == null) {
            throw new RuntimeException("入库单ID不能为空");
        }
        Inbound inbound = inboundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("入库单不存在"));

        // 恢复库存
        Product product = inbound.getProduct();
        if (product != null) {
            product.setStockQty(product.getStockQty() - inbound.getQuantity());
            productRepository.save(product);
        }

        inboundRepository.deleteById(id);
    }

    /**
     * 导出入库记录到Excel
     */
    public byte[] exportToExcel(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<Inbound> inbounds;
        if (startDate != null && endDate != null) {
            inbounds = inboundRepository.findByInboundDateBetween(startDate, endDate);
        } else {
            inbounds = inboundRepository.findAll();
        }

        String[] headers = {"入库单号", "商品编号", "商品名称", "入库数量", "供应商", "入库日期", "操作员", "备注"};

        return ExcelUtil.exportExcel(inbounds, headers,
                Inbound::getInboundNo,
                i -> i.getProduct() != null ? i.getProduct().getCode() : "",
                i -> i.getProduct() != null ? i.getProduct().getName() : "",
                Inbound::getQuantity,
                i -> i.getSupplier() != null ? i.getSupplier().getName() : "",
                Inbound::getInboundDate,
                i -> i.getOperator() != null ? i.getOperator().getUsername() : "",
                i -> i.getRemark() != null ? i.getRemark() : ""
        );
    }
}

