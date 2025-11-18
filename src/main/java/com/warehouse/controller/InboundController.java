package com.warehouse.controller;

import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.Inbound;
import com.warehouse.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        PageResult<Inbound> result = inboundService.getInbounds(
                startDate, endDate, productId, supplierId, page, size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Inbound> getInboundById(@PathVariable Integer id) {
        Inbound inbound = inboundService.getInboundById(id);
        return Result.success(inbound);
    }

    @PostMapping
    public Result<Inbound> createInbound(@RequestBody Inbound inbound) {
        Inbound created = inboundService.createInbound(inbound);
        return Result.success(created);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteInbound(@PathVariable Integer id) {
        inboundService.deleteInbound(id);
        return Result.success(null);
    }

    /**
     * 导出入库记录到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            byte[] data = inboundService.exportToExcel(startDate, endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "inbounds.xlsx");

            return ResponseEntity.ok().headers(headers).body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

