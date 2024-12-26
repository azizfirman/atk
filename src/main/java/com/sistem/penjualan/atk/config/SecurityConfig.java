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

    // @Bean
    // @Primary
    // UserDetailsService userDetailsService() {
    //     return new CustomUserDetailService();
    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
             // Authorize request
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(Constant.Security.permitAll()).permitAll()

            // .requestMatchers("/api/barang/**", "/api/pelanggan/**", "/api/transaksi/**").hasAnyRole("ADMIN", "USER")
            // .requestMatchers("/", "/login", "/api-docs/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/api/pengguna/**").hasRole("ADMIN")
            .anyRequest().authenticated())

            // Configure session management
            .sessionManagement(sessionManagement -> sessionManagement
            .maximumSessions(1).expiredUrl("/login?expired"))

            // Configure form login and logout
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/login?error=true")
                // .defaultSuccessUrl("/home", true)
                .permitAll())
            .logout(logout -> logout.permitAll())
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}