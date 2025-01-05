package com.sistem.penjualan.atk.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
public class Barang {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idBarang;

    @NotBlank()
    private String namaBarang;

    @Positive
    private double hargaBarang;

    @PositiveOrZero
    private int stokBarang;

    @ManyToOne
    @JoinColumn(name = "idPengguna")
    private Pengguna pengguna;

    @ManyToOne
    @JoinColumn(name = "idPelanggan")
    private Pelanggan pelanggan;

    @NotNull
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updateAt;
}