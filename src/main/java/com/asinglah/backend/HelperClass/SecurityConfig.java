package com.asinglah.backend.HelperClass;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // 12 = strength (work factor)
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
       .requestMatchers(
    "/swagger-ui.html",
    "/error",
    "/swagger-ui/**",
    "/v3/api-docs",    
    "/v3/api-docs/**",
    "/swagger-resources/**",
    "/swagger-resources",
    "/configuration/ui",
    "/configuration/security",
    "/webjars/**",
    "/api/users/signup",
    "/api/users/login",
    "/api/expense/createExpenseGroupID",
    "/api/users/signup/**",
    "/api/users/**",
    "/api/users/login/**"
        ).permitAll()
        .anyRequest().authenticated()
    ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
    .httpBasic(withDefaults())
    ; // instead of formLogin

        return http.build();
    }
}
