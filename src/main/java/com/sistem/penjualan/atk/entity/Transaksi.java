package com.sistem.penjualan.atk.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idTransaksi;

    @NotNull()
    private String nomorTransaksi;

    @NotNull()
    private LocalDate tanggalTransaksi;

    @ManyToOne
    @JoinColumn(name = "idPelanggan")
    private Pelanggan pelanggan;

    @ManyToOne
    @JoinColumn(name = "idBarang")
    private Barang barang;

    @Positive
    private int jumlah;

    @ManyToOne
    @JoinColumn(name = "idPengguna")
    private Pengguna pengguna;

    @NotNull
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updateAt;
}