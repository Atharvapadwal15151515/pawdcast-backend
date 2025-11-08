package com.pawdcast.pawdcast.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pawdcast.pawdcast.application.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // ========== PUBLIC ENDPOINTS ==========
                .requestMatchers(
                    // Authentication endpoints
                    "/auth/signup",
                    "/auth/login", 
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
                    "/api/pets/**",
                    "/pet-diet/**",
                    "/api/insurance/**",
                    "/api/legal/**",
                    "/api/products/**",
                    "/stats/**",
                    "/api/training-tips/**",
                    
                    // Pet browsing (public)
                    "/pets/all",
                    "/pets/available",
                    "/pets/**/photo",
                    "/pets/**/medical-records",
                    "/pets/**/vaccination-certificate", 
                    "/pets/**/ownership-proof",
                    "/pets/**/residence-proof",
                    "/pets/**/surrender-agreement",
                    
                    // Breed information (public)
                    "/api/breeds",
                    "/api/breeds/**",
                    
                    // Categories (public)
                    "/api/categories",
                    "/api/categories/main",
                    "/api/categories/**/products",
                    "/api/categories/**/subcategories",
                    
                    // Static resources
                    "/css/**",
                    "/js/**", 
                    "/images/**",
                    "/",
                    "/error"
                ).permitAll()
                
                // ========== SECURED ENDPOINTS (Require JWT) ==========
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
                
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of(
            "https://pawdcast.netlify.app",
            "http://localhost:3000",
            "http://localhost:5173",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:5173"
        ));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setExposedHeaders(java.util.List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}