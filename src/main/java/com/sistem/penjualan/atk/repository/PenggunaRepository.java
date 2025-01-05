package com.sistem.penjualan.atk.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistem.penjualan.atk.entity.Pengguna;

@Repository
public interface PenggunaRepository extends JpaRepository<Pengguna, UUID> {

    boolean existsByUsername(String username);

    Optional<Pengguna> findByUsername(String username);

    Page<Pengguna> findByNamaPenggunaContainingIgnoreCase(String namaPengguna, Pageable pageable);

    List<Pengguna> findByNamaPenggunaContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrLevelContainingIgnoreCase(String namaPengguna, String username, String level);
}