package com.warehouse.controller;

import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.StockAdjustment;
import com.warehouse.service.StockAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存调整控制器，提供调整记录查询与新增。
 */
@RestController
@RequestMapping("/stock-adjustments")
@CrossOrigin
public class StockAdjustmentController {
    @Autowired
    private StockAdjustmentService stockAdjustmentService;

    @GetMapping
    public Result<PageResult<StockAdjustment>> getAdjustments(
            @RequestParam(required = false) Integer productId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<StockAdjustment> result = stockAdjustmentService.getAdjustments(productId, page, size);
        return Result.success(result);
    }

    @PostMapping
    public Result<StockAdjustment> createAdjustment(@RequestBody StockAdjustment adjustment) {
        StockAdjustment created = stockAdjustmentService.createAdjustment(adjustment);
        return Result.success(created);
    }
}

