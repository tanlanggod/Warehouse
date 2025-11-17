package com.warehouse.controller;

import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.Outbound;
import com.warehouse.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 出库接口控制器，处理出库单的增删查。
 */
@RestController
@RequestMapping("/outbounds")
@CrossOrigin
public class OutboundController {
    @Autowired
    private OutboundService outboundService;

    @GetMapping
    public Result<PageResult<Outbound>> getOutbounds(
            @RequestParam(required = false)  LocalDateTime startDate,
            @RequestParam(required = false)  LocalDateTime endDate,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<Outbound> result = outboundService.getOutbounds(
                    startDate, endDate, productId, customerId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Outbound> getOutboundById(@PathVariable Integer id) {
        try {
            Outbound outbound = outboundService.getOutboundById(id);
            return Result.success(outbound);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<Outbound> createOutbound(@RequestBody Outbound outbound) {
        try {
            Outbound created = outboundService.createOutbound(outbound);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOutbound(@PathVariable Integer id) {
        try {
            outboundService.deleteOutbound(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

