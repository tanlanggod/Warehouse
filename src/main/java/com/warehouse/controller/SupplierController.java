package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Supplier;
import com.warehouse.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商控制器，提供供应商数据管理接口。
 */
@RestController
@RequestMapping("/suppliers")
@CrossOrigin
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public Result<List<Supplier>> getAllSuppliers() {
        return Result.success(supplierRepository.findAll());
    }

    @PostMapping
    public Result<Supplier> createSupplier(@RequestBody Supplier supplier) {
        try {
            return Result.success(supplierRepository.save(supplier));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Supplier> updateSupplier(@PathVariable Integer id, @RequestBody Supplier supplier) {
        try {
            Supplier existing = supplierRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("供应商不存在"));
            existing.setName(supplier.getName());
            existing.setContactPerson(supplier.getContactPerson());
            existing.setPhone(supplier.getPhone());
            existing.setAddress(supplier.getAddress());
            existing.setEmail(supplier.getEmail());
            existing.setStatus(supplier.getStatus());
            return Result.success(supplierRepository.save(existing));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteSupplier(@PathVariable Integer id) {
        try {
            supplierRepository.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

