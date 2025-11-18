package com.warehouse.service;

import com.warehouse.common.PageResult;
import com.warehouse.entity.Category;
import com.warehouse.entity.Product;
import com.warehouse.entity.Supplier;
import com.warehouse.repository.CategoryRepository;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.SupplierRepository;
import com.warehouse.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品业务服务，封装商品的增删改查及库存告警逻辑。
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public PageResult<Product> getProducts(String code, String name, Integer categoryId, Integer status,
                                           Integer minStockQty, Integer maxStockQty,
                                           Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productRepository.findByConditions(
                code, name, categoryId, status, minStockQty, maxStockQty, pageable);
        return new PageResult<>(productPage.getTotalElements(), productPage.getContent());
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
    }

    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsByCode(product.getCode())) {
            throw new RuntimeException("商品编号已存在");
        }
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            product.setCategory(categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("商品类别不存在")));
        }
        if (product.getSupplier() != null && product.getSupplier().getId() != null) {
            product.setSupplier(supplierRepository.findById(product.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("供应商不存在")));
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Integer id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (!existingProduct.getCode().equals(product.getCode()) &&
            productRepository.existsByCode(product.getCode())) {
            throw new RuntimeException("商品编号已存在");
        }

        existingProduct.setName(product.getName());
        existingProduct.setCode(product.getCode());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setMinStock(product.getMinStock());
        existingProduct.setUnit(product.getUnit());
        existingProduct.setBarcode(product.getBarcode());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setStatus(product.getStatus());

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            existingProduct.setCategory(categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("商品类别不存在")));
        }
        if (product.getSupplier() != null && product.getSupplier().getId() != null) {
            existingProduct.setSupplier(supplierRepository.findById(product.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("供应商不存在")));
        }

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("商品不存在");
        }
        productRepository.deleteById(id);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    /**
     * 导出商品数据到Excel
     */
    public byte[] exportToExcel() throws IOException {
        List<Product> products = productRepository.findAll();

        String[] headers = {"商品ID", "商品编号", "商品名称", "分类", "供应商", "单价", "库存数量", "最低库存", "单位", "条形码", "状态"};

        return ExcelUtil.exportExcel(products, headers,
                Product::getId,
                Product::getCode,
                Product::getName,
                p -> p.getCategory() != null ? p.getCategory().getName() : "",
                p -> p.getSupplier() != null ? p.getSupplier().getName() : "",
                Product::getPrice,
                Product::getStockQty,
                Product::getMinStock,
                Product::getUnit,
                p -> p.getBarcode() != null ? p.getBarcode() : "",
                p -> p.getStatus() == 1 ? "启用" : "禁用"
        );
    }

    /**
     * 从Excel导入商品数据
     */
    @Transactional
    public List<String> importFromExcel(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();

        List<Product> products = ExcelUtil.importExcel(file.getInputStream(), 1, (row, rowNum) -> {
            try {
                Product product = new Product();

                // 商品编号（必填）
                String code = ExcelUtil.getCellValue(row.getCell(0));
                if (code == null || code.trim().isEmpty()) {
                    errors.add("第" + (rowNum + 1) + "行：商品编号不能为空");
                    return null;
                }
                product.setCode(code.trim());

                // 商品名称（必填）
                String name = ExcelUtil.getCellValue(row.getCell(1));
                if (name == null || name.trim().isEmpty()) {
                    errors.add("第" + (rowNum + 1) + "行：商品名称不能为空");
                    return null;
                }
                product.setName(name.trim());

                // 分类名称
                String categoryName = ExcelUtil.getCellValue(row.getCell(2));
                if (categoryName != null && !categoryName.trim().isEmpty()) {
                    Category category = categoryRepository.findByName(categoryName.trim());
                    if (category != null) {
                        product.setCategory(category);
                    }
                }

                // 供应商名称
                String supplierName = ExcelUtil.getCellValue(row.getCell(3));
                if (supplierName != null && !supplierName.trim().isEmpty()) {
                    Supplier supplier = supplierRepository.findByName(supplierName.trim());
                    if (supplier != null) {
                        product.setSupplier(supplier);
                    }
                }

                // 单价
                String priceStr = ExcelUtil.getCellValue(row.getCell(4));
                if (priceStr != null && !priceStr.trim().isEmpty()) {
                    try {
                        product.setPrice(new BigDecimal(priceStr.trim()));
                    } catch (NumberFormatException e) {
                        product.setPrice(BigDecimal.ZERO);
                    }
                }

                // 最低库存
                String minStockStr = ExcelUtil.getCellValue(row.getCell(5));
                if (minStockStr != null && !minStockStr.trim().isEmpty()) {
                    try {
                        product.setMinStock(Integer.parseInt(minStockStr.trim().split("\\.")[0]));
                    } catch (NumberFormatException e) {
                        product.setMinStock(0);
                    }
                }

                // 单位
                String unit = ExcelUtil.getCellValue(row.getCell(6));
                if (unit != null && !unit.trim().isEmpty()) {
                    product.setUnit(unit.trim());
                }

                // 条形码
                String barcode = ExcelUtil.getCellValue(row.getCell(7));
                if (barcode != null && !barcode.trim().isEmpty()) {
                    product.setBarcode(barcode.trim());
                }

                // 说明
                String description = ExcelUtil.getCellValue(row.getCell(8));
                if (description != null && !description.trim().isEmpty()) {
                    product.setDescription(description.trim());
                }

                return product;
            } catch (Exception e) {
                errors.add("第" + (rowNum + 1) + "行：数据格式错误 - " + e.getMessage());
                return null;
            }
        });

        // 保存商品数据
        for (Product product : products) {
            try {
                if (productRepository.existsByCode(product.getCode())) {
                    errors.add("商品编号 " + product.getCode() + " 已存在，跳过");
                } else {
                    productRepository.save(product);
                }
            } catch (Exception e) {
                errors.add("商品 " + product.getCode() + " 保存失败：" + e.getMessage());
            }
        }

        return errors;
    }
}

