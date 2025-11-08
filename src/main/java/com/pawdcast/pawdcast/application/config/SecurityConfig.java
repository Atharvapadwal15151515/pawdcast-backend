package com.pawdcast.pawdcast.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.pawdcast.pawdcast.application.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtFilter jwtFilter;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource, JwtFilter jwtFilter) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Enable CORS
            .csrf(csrf -> csrf.disable()) // Disable CSRF for JWT
            .authorizeHttpRequests(authz -> authz
                // ========== PUBLIC ENDPOINTS (No authentication required) ==========
                .requestMatchers(
                    // Authentication endpoints
                    "/auth/login",
                    "/auth/signup", 
                    "/auth/check",
                    "/auth/password/**",
                    
                    // User registration endpoints
                    "/api/auth/register",
                    "/api/auth/register/full", 
                    "/api/auth/login",
                    "/users/signup",
                    "/users/login",
                    
                    // Public information endpoints
                    "/api/pet-care",
                    "/api/pets",
                    "/api/pets/**",
                    "/pet-diet/**",
                    "/legal/**",
                    "/api/insurance/**",
                    "/api/legal/**",
                    "/api/products/**",
                    "/stats/**",
                    "/api/training-tips/**",
                    "/venues/**",
                    "/clinics/**",
                    "/api/breeds/**",
                    "/api/breeds",
                    "/api/breeds/type/**",
                    "/api/breeds/search",
                    "/api/breeds/recommend/**",
                    "/api/breeds/allergy-friendly/**",
                    "/api/breeds/health",
                    "/api/categories",
                    "/api/categories/**",
                    
                    // Pet browsing (public listings)
                    "/pets/all",
                    "/pets/available",
                    "/pets/**/photo",
                    "/pets/**/medical-records", 
                    "/pets/**/vaccination-certificate",
                    "/pets/**/ownership-proof",
                    "/pets/**/residence-proof",
                    "/pets/**/surrender-agreement",
                    
                    // Static resources
                    "/css/**",
                    "/js/**", 
                    "/images/**",
                    "/"
                ).permitAll()
                
                // ========== SECURED ENDPOINTS (Require JWT authentication) ==========
                .requestMatchers(
                    // User management
                    "/auth/me",
                    "/auth/profile", 
                    "/auth/logout",
                    "/users/me",
                    "/users/**",
                    "/api/auth/password",
                    "/api/auth/profile",
                    "/api/auth/**",
                    
                    // Pet management
                    "/pet-profiles/**",
                    "/pets/add",
                    
                    // Health & habits
                    "/pet-health/**",
                    "/api/habits/**",
                    "/food-entries/**",
                    
                    // Adoptions & seekers
                    "/adoptions/**", 
                    "/seekers/**",
                    
                    // E-commerce
                    "/api/cart/**",
                    "/api/orders/**",
                    
                    // Personal data
                    "/api/diary/**",
                    "/api/digilocker/**",
                    "/api/expenses/**"
                ).authenticated()
                
                // ========== ADMIN ENDPOINTS (Future role-based access) ==========
                .requestMatchers(
                    "/api/breeds", 
                    "/api/breeds/**"
                ).hasAnyRole("ADMIN")
                
                // Category management (secured for authenticated users)
                .requestMatchers(
                    "/api/categories"
                ).authenticated()
                
                // Allow all other requests (adjust based on your needs)
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions - JWT only
            )
            // Add JWT filter before other filters
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}