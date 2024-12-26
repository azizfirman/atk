package com.sistem.penjualan.atk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sistem.penjualan.atk.util.Constant;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            // Authorize request
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(Constant.Security.permitAll()).permitAll()
            .anyRequest().authenticated())

            // Configure session management
            .sessionManagement(sessionManagement -> sessionManagement
            .maximumSessions(1).expiredUrl("/login?expired"))

            // Configure form login and logout
            .formLogin(formLogin -> formLogin
            .defaultSuccessUrl("/barang", true)
            .failureUrl("/login?error=true")
            .loginPage("/login")
            .permitAll())

            // Configure logout and build
            .logout(logout -> logout.permitAll())
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}