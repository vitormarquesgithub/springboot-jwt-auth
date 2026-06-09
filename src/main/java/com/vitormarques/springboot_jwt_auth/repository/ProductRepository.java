package com.vitormarques.springboot_jwt_auth.repository;

import com.vitormarques.springboot_jwt_auth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}