package com.mhridin.pts_subscription_service.controller;

import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.exception.ProductNotFoundException;
import com.mhridin.pts_common.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault(sort = "title", direction = Sort.Direction.ASC)
                                                            Pageable pageable) {
        Page<Product> all = productRepository.findAll(pageable);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found")));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        product.setLastUpdated(LocalDateTime.now());
        product.setCurrentPrice(null);
        product.setIsAvailable(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        if (!Objects.equals(product.getId(), id)) {
            throw new IllegalStateException("Product id and path variable are not the same");
        }
        Product fromDB = productRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        fromDB.setUrl(product.getUrl());
        fromDB.setTitle(product.getTitle());
        productRepository.save(fromDB);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        Product fromDB = productRepository.findById(id).orElse(null);
        if (fromDB == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepository.delete(fromDB);
        return ResponseEntity.noContent().build();
    }
}
