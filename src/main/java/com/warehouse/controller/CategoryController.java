package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Category;
import com.warehouse.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品类别控制器，提供类别增删改查接口。
 */
@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryRepository.findAll());
    }

    @PostMapping
    public Result<Category> createCategory(@RequestBody Category category) {
        try {
            if (categoryRepository.existsByName(category.getName())) {
                return Result.error("类别名称已存在");
            }
            return Result.success(categoryRepository.save(category));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Category> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        try {
            Category existing = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("类别不存在"));
            if (!existing.getName().equals(category.getName()) &&
                categoryRepository.existsByName(category.getName())) {
                return Result.error("类别名称已存在");
            }
            existing.setName(category.getName());
            existing.setDescription(category.getDescription());
            return Result.success(categoryRepository.save(existing));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Integer id) {
        try {
            categoryRepository.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

