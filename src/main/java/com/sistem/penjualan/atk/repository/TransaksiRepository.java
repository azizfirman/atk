package com.sistem.penjualan.atk.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sistem.penjualan.atk.entity.Transaksi;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, UUID>, JpaSpecificationExecutor<Transaksi> {

    boolean existsByBarang_IdBarang(UUID idBarang);

    List<Transaksi> findByOrderByBarang_NamaBarangAsc();

    boolean existsByPengguna_IdPengguna(UUID idPengguna);

    boolean existsByPelanggan_IdPelanggan(UUID idPelanggan);

    List<Transaksi> findByTanggalTransaksi(LocalDate nomorTransaksi);

    List<Transaksi> findByTanggalTransaksiBetween(LocalDate startDate, LocalDate endDate);

    List<Transaksi> findByPengguna_IdPenggunaAndTanggalTransaksiBetween(UUID idPengguna, LocalDate startDate, LocalDate endDate);

    Page<Transaksi> findByBarang_NamaBarangContainingIgnoreCaseAndPelanggan_NamaPelangganContainingIgnoreCaseAndPengguna_NamaPenggunaContainingIgnoreCase(String namaBarang, String namaPelanggan, String namaPengguna, Pageable pageable);

    Page<Transaksi> findByTanggalTransaksiBetweenAndBarang_NamaBarangContainingIgnoreCaseAndPelanggan_NamaPelangganContainingIgnoreCaseAndPengguna_NamaPenggunaContainingIgnoreCase(LocalDate startDate, LocalDate endDate, String namaBarang, String namaPelanggan, String namaPengguna, Pageable pageable);
}