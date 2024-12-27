package com.sistem.penjualan.atk.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistem.penjualan.atk.entity.Pelanggan;

@Repository
public interface PelangganRepository extends JpaRepository<Pelanggan, UUID> {

}