package com.vitormarques.springboot_jwt_auth.service;

import com.vitormarques.springboot_jwt_auth.dto.ProductRequest;
import com.vitormarques.springboot_jwt_auth.dto.ProductResponse;
import com.vitormarques.springboot_jwt_auth.entity.Product;
import com.vitormarques.springboot_jwt_auth.repository.ProductRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProductResponse findById(@NonNull UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return toResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ProductResponse update(@NonNull UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        Product updated = productRepository.save(product);
        return toResponse(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(@NonNull UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}