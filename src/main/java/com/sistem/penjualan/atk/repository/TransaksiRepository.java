package com.sistem.penjualan.atk.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistem.penjualan.atk.entity.Transaksi;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, UUID> {

}