package com.sistem.penjualan.atk.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistem.penjualan.atk.entity.Pengguna;

@Repository
public interface PenggunaRepository extends JpaRepository<Pengguna, UUID> {

    Optional<Pengguna> findByUsername(String username);

}