package com.vitormarques.springboot_jwt_auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitormarques.springboot_jwt_auth.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}