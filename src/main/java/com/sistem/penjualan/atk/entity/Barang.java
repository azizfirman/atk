package com.sistem.penjualan.atk.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Barang {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idBarang;
    private String namaBarang;
    private double hargaBarang;
    private String photo;
    private int stok;

    @ManyToOne
    @JoinColumn(name = "idPengguna")
    private Pengguna pengguna;
}