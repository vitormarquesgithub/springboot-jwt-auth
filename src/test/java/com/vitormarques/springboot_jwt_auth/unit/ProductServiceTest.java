package com.vitormarques.springboot_jwt_auth.unit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vitormarques.springboot_jwt_auth.dto.ProductRequest;
import com.vitormarques.springboot_jwt_auth.dto.ProductResponse;
import com.vitormarques.springboot_jwt_auth.entity.Product;
import com.vitormarques.springboot_jwt_auth.repository.ProductRepository;
import com.vitormarques.springboot_jwt_auth.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);
    }

    @Test
    void shouldFindAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> products = productService.findAll();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    void shouldFindProductById() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(productId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productId);
    }

    @Test
    void shouldFailFindProductByIdNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(productId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found with id: " + productId);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("New Product");
        request.setDescription("New Description");
        request.setPrice(new BigDecimal("49.99"));
        request.setStock(5);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Test Product");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldUpdateProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Updated Product");
        request.setDescription("Updated Description");
        request.setPrice(new BigDecimal("149.99"));
        request.setStock(20);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("149.99"));
        updatedProduct.setStock(20);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.update(productId, request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Updated Product");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldFailUpdateProductNotFound() {
        ProductRequest request = new ProductRequest();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(productId, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found with id: " + productId);
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.delete(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldFailDeleteProductNotFound() {
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThatThrownBy(() -> productService.delete(productId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found with id: " + productId);
    }
}