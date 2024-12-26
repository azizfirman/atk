package com.sistem.penjualan.atk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.repository.PenggunaRepository;

@Service
@Primary
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private PenggunaRepository penggunaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Pengguna pengguna = penggunaRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Pengguna tidak ada"));

        return User.builder()
                    .username(pengguna.getUsername())
                    .password(pengguna.getPassword())
                    .roles(pengguna.getLevel())
                    .build();
    }

}
