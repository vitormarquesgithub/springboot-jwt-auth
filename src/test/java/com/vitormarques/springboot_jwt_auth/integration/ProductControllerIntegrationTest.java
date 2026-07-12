package com.vitormarques.springboot_jwt_auth.integration;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitormarques.springboot_jwt_auth.config.SecurityConfig;
import com.vitormarques.springboot_jwt_auth.controller.ProductController;
import com.vitormarques.springboot_jwt_auth.dto.ProductRequest;
import com.vitormarques.springboot_jwt_auth.dto.ProductResponse;
import com.vitormarques.springboot_jwt_auth.security.JwtAuthenticationFilter;
import com.vitormarques.springboot_jwt_auth.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void bypassJwtFilter() throws Exception {
        org.mockito.Mockito.doAnswer(invocation -> {
            jakarta.servlet.ServletRequest request = invocation.getArgument(0);
            jakarta.servlet.ServletResponse response = invocation.getArgument(1);
            jakarta.servlet.FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowUserToListProducts() throws Exception {
        ProductResponse response = ProductResponse.builder()
                .id(UUID.randomUUID())
                .name("Product")
                .price(new BigDecimal("99.99"))
                .stock(10)
                .build();
        when(productService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("New Product");
        request.setDescription("Description");
        request.setPrice(new BigDecimal("49.99"));
        request.setStock(5);

        ProductResponse response = ProductResponse.builder()
                .id(UUID.randomUUID())
                .name("New Product")
                .price(new BigDecimal("49.99"))
                .stock(5)
                .build();

        when(productService.create(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyUserToCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("New Product");
        request.setDescription("Description");
        request.setPrice(new BigDecimal("49.99"));
        request.setStock(5);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}