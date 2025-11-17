package com.warehouse.controller;

import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.Inbound;
import com.warehouse.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 入库接口控制器，提供入库单的增删查操作。
 */
@RestController
@RequestMapping("/inbounds")
@CrossOrigin
public class InboundController {
    @Autowired
    private InboundService inboundService;

    @GetMapping
    public Result<PageResult<Inbound>> getInbounds(
            @RequestParam(required = false)  LocalDateTime startDate,
            @RequestParam(required = false)  LocalDateTime endDate,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer supplierId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<Inbound> result = inboundService.getInbounds(
                    startDate, endDate, productId, supplierId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Inbound> getInboundById(@PathVariable Integer id) {
        try {
            Inbound inbound = inboundService.getInboundById(id);
            return Result.success(inbound);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<Inbound> createInbound(@RequestBody Inbound inbound) {
        try {
            Inbound created = inboundService.createInbound(inbound);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteInbound(@PathVariable Integer id) {
        try {
            inboundService.deleteInbound(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

