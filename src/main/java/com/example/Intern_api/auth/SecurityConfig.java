package com.example.Intern_api.auth;

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

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter)
            throws Exception {
        return http
                // .csrf(AbstractHttpConfigurer::disable) // Vẫn giữ disable CSRF nếu không dùng
                // .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Sử dụng cấu hình CORS
                // .sessionManagement(session -> session
                //         .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Hỗ trợ Remember Me
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/at/api/user/login", "/at/api/user/getUserById/").permitAll()
                        .requestMatchers("/at/api/user/").hasAuthority("ADMIN")  // Only Admin can access all user APIs
                        .anyRequest().authenticated()
                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // .rememberMe(remember -> remember
                //         .key("R3m3mb3rM3$ecureKey")
                //         .rememberMeCookieName("myRememberMeCookie")
                //         .tokenValiditySeconds(7 * 24 * 60 * 60)
                // )
                // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS configuration bean
    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    //     configuration.setAllowCredentials(true);
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }
}
