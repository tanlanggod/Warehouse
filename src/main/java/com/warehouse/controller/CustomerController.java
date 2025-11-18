package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Customer;
import com.warehouse.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器，提供客户信息管理接口。
 */
@RestController
@RequestMapping("/customers")
@CrossOrigin
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public Result<List<Customer>> getAllCustomers() {
        return Result.success(customerRepository.findAll());
    }

    @PostMapping
    public Result<Customer> createCustomer(@RequestBody Customer customer) {
        return Result.success(customerRepository.save(customer));
    }

    @PutMapping("/{id}")
    public Result<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        existing.setName(customer.getName());
        existing.setContactPerson(customer.getContactPerson());
        existing.setPhone(customer.getPhone());
        existing.setAddress(customer.getAddress());
        existing.setEmail(customer.getEmail());
        existing.setStatus(customer.getStatus());
        return Result.success(customerRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@PathVariable Integer id) {
        customerRepository.deleteById(id);
        return Result.success(null);
    }
}

