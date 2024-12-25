package com.sistem.penjualan.atk.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistem.penjualan.atk.entity.Pengguna;

public interface PenggunaRepository extends JpaRepository<Pengguna, UUID> {
    Pengguna findByUsername(String username);
}