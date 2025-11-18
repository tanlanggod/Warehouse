package com.warehouse.controller;

import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.Outbound;
import com.warehouse.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        PageResult<Outbound> result = outboundService.getOutbounds(
                startDate, endDate, productId, customerId, page, size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Outbound> getOutboundById(@PathVariable Integer id) {
        Outbound outbound = outboundService.getOutboundById(id);
        return Result.success(outbound);
    }

    @PostMapping
    public Result<Outbound> createOutbound(@RequestBody Outbound outbound) {
        Outbound created = outboundService.createOutbound(outbound);
        return Result.success(created);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOutbound(@PathVariable Integer id) {
        outboundService.deleteOutbound(id);
        return Result.success(null);
    }

    /**
     * 导出出库记录到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            byte[] data = outboundService.exportToExcel(startDate, endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "outbounds.xlsx");

            return ResponseEntity.ok().headers(headers).body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

