package com.vitormarques.springboot_jwt_auth.config;

import com.vitormarques.springboot_jwt_auth.entity.Role;
import com.vitormarques.springboot_jwt_auth.entity.User;
import com.vitormarques.springboot_jwt_auth.repository.RoleRepository;
import com.vitormarques.springboot_jwt_auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder,
                            Environment environment) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Skip admin creation in test profile
        if (environment != null && environment.matchesProfiles("test")) {
            return;
        }

        // For dev profile, use default password if not set
        if (environment != null && environment.matchesProfiles("dev") &&
            (adminPassword == null || adminPassword.isBlank())) {
            adminPassword = "devAdmin123!";
            System.out.println("⚠️  DEVELOPMENT MODE: Using default admin password");
        }

        createAdminUserIfNotExists();
    }

    private void createAdminUserIfNotExists() {
        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                "Admin password must be set via environment variable APP_ADMIN_PASSWORD"
            );
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found in database"));

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}