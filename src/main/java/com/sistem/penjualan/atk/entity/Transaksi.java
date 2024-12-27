package com.sistem.penjualan.atk.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idTransaksi;

    @NotNull()
    private LocalDate tanggalTranskasi;

    @ManyToOne
    @JoinColumn(name = "idPelanggan")
    private Pelanggan pelanggan;

    @ManyToOne
    @JoinColumn(name = "idBarang")
    private Barang barang;
}