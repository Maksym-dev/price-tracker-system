package com.mhridin.pts_subscription_service.controller;

import com.mhridin.pts_common.entity.Product;
import com.mhridin.pts_common.exception.ProductNotFoundException;
import com.mhridin.pts_common.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductRestController productRestController;

    @Test
    void testGetAllProducts() {
        List<Product> all = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        all.add(product);

        when(productRepository.findAll()).thenReturn(all);

        List<Product> allProducts = productRestController.getAllProducts();

        verify(productRepository, times(1)).findAll();
        assertThat(allProducts).isNotEmpty();
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        long productId = 1L;
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ResponseEntity<Product> subscriptionById = productRestController.getProductById(productId);

        verify(productRepository, times(1)).findById(productId);
        Product result = subscriptionById.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
    }

    @Test
    void testGetProductByIdThrowProductNotFoundException() {
        long productId = 1L;

        when(productRepository.findById(productId)).thenThrow(new ProductNotFoundException("Product with id " + productId + " not found"));

        assertThrows(ProductNotFoundException.class, () -> productRestController.getProductById(productId));

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {
        String url = "https://example.com/item";
        Product product = new Product();
        product.setId(10L);
        product.setUrl(url);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productRestController.createProduct(product);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("titleNew");
        Product fromDb = new Product();
        fromDb.setId(1L);
        fromDb.setTitle("titleDb");

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(fromDb));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productRestController.updateProduct(1L, product);

        ArgumentCaptor<Product> eventCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).save(eventCaptor.capture());

        Product captured = eventCaptor.getValue();
        assertThat(captured.getId()).isEqualTo(1L);
        assertThat(captured.getTitle()).isEqualTo(product.getTitle());
    }

    @Test
    void testUpdateProductThrowsIllegalStateException() {
        Product product = new Product();
        product.setId(1L);

        assertThrows(IllegalStateException.class, () -> productRestController.updateProduct(2L, product));

        verify(productRepository, times(0)).findById(product.getId());
        verify(productRepository, times(0)).save(product);
    }

    @Test
    void testUpdateProductThrowsProductNotFoundException() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productRestController.updateProduct(1L, product));

        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(0)).save(product);
    }

    @Test
    void testDeleteProduct() {
        Product fromDb = new Product();
        fromDb.setId(1L);

        when(productRepository.findById(fromDb.getId())).thenReturn(Optional.of(fromDb));

        productRestController.deleteProduct(1L);

        verify(productRepository, times(1)).findById(fromDb.getId());
        verify(productRepository, times(1)).delete(fromDb);
    }

    @Test
    void testDeleteProductThrowsProductNotFoundException() {
        Product fromDb = new Product();
        fromDb.setId(1L);

        when(productRepository.findById(fromDb.getId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productRestController.deleteProduct(1L));

        verify(productRepository, times(1)).findById(fromDb.getId());
        verify(productRepository, times(0)).delete(fromDb);
    }
}