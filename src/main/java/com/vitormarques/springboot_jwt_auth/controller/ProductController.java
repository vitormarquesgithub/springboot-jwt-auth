package com.vitormarques.springboot_jwt_auth.controller;

import com.vitormarques.springboot_jwt_auth.dto.ProductRequest;
import com.vitormarques.springboot_jwt_auth.dto.ProductResponse;
import com.vitormarques.springboot_jwt_auth.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "CRUD operations for products")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List all products (USER or ADMIN)")
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID (USER or ADMIN)")
    public ResponseEntity<ProductResponse> getById(@PathVariable @NonNull UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new product (ADMIN only)")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product (ADMIN only)")
    public ResponseEntity<ProductResponse> update(@PathVariable @NonNull UUID id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product (ADMIN only)")
    public ResponseEntity<Void> delete(@PathVariable @NonNull UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}