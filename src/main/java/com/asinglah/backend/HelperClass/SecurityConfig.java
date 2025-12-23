package com.asinglah.backend.HelperClass;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    .cors(withDefaults()) 
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
    //"/api/users/**",
    "/api/users/login/**"
        ).permitAll()
        .anyRequest().authenticated()
    ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
    .httpBasic(withDefaults())
    ; // instead of formLogin

        return http.build();
    }

    @Bean 
    public CorsConfigurationSource corsConfigurationSource() 
    { 
        CorsConfiguration config = new CorsConfiguration(); 
         
        config.setAllowCredentials(true); 
        //add the allow origin
        //config.addAllowedOriginPattern("http://localhost:4200");
        config.addAllowedOrigin("http://localhost:4200"); 
        config.addAllowedHeader("*"); 
        config.addAllowedMethod("*"); 
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); 
        return source; 
    }
}
