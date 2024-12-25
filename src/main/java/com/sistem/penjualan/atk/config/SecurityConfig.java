package com.sistem.penjualan.atk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/pengguna/**").permitAll() // Izinkan akses ke API Pengguna
                .requestMatchers("/pengguna").permitAll() // Izinkan akses ke halaman pengguna
                .anyRequest().authenticated()
            )
            .httpBasic(); // Basic authentication untuk endpoint lain
        return http.build();
    }
}
