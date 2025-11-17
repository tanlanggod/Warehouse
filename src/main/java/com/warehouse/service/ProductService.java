package com.warehouse.service;

import com.warehouse.common.PageResult;
import com.warehouse.entity.Product;
import com.warehouse.repository.CategoryRepository;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PageResult<Product> getProducts(String name, Integer categoryId, Integer status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productRepository.findByConditions(name, categoryId, status, pageable);
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
}

