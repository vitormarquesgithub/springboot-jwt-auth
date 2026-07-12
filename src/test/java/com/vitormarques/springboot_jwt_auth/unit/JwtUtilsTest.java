package com.vitormarques.springboot_jwt_auth.unit;

import com.vitormarques.springboot_jwt_auth.security.JwtKeyManager;
import com.vitormarques.springboot_jwt_auth.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilsTest {

    private static final String TEST_SECRET = "mySuperSecretKeyForHS256ThatIsAtLeast32CharsLong";
    private JwtUtils jwtUtils;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        JwtKeyManager keyManager = new JwtKeyManager(TEST_SECRET);
        jwtUtils = new JwtUtils(keyManager, 3600000L, 604800000L);
        userDetails = User.withUsername("testuser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void shouldGenerateAccessToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "123");
        claims.put("roles", "ROLE_USER");

        String token = jwtUtils.generateAccessToken(userDetails, claims);

        assertThat(token).isNotBlank();
        assertThat(jwtUtils.extractUsername(token)).isEqualTo("testuser");
    }

    @Test
    void shouldGenerateRefreshToken() {
        String token = jwtUtils.generateRefreshToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(jwtUtils.extractUsername(token)).isEqualTo("testuser");
    }

    @Test
    void shouldExtractExpiration() {
        String token = jwtUtils.generateAccessToken(userDetails, new HashMap<>());
        
        assertThat(jwtUtils.extractExpiration(token)).isNotNull();
    }

    @Test
    void shouldValidateToken() {
        String token = jwtUtils.generateAccessToken(userDetails, new HashMap<>());
        
        boolean isValid = jwtUtils.isTokenValid(token, userDetails);
        
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldInvalidateTokenWithWrongUser() {
        String token = jwtUtils.generateAccessToken(userDetails, new HashMap<>());
        UserDetails wrongUser = User.withUsername("wronguser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        boolean isValid = jwtUtils.isTokenValid(token, wrongUser);
        
        assertThat(isValid).isFalse();
    }
}