package com.sistem.penjualan.atk.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistem.penjualan.atk.entity.Barang;

@Repository
public interface BarangRepository extends JpaRepository<Barang, UUID> {

    long countByStokBarangGreaterThan(int stokBarang);

    long countByStokBarangLessThanEqual(int stokBarang);

    Page<Barang> findByNamaBarangContainingIgnoreCase(String namaBarang, Pageable pageable);
}