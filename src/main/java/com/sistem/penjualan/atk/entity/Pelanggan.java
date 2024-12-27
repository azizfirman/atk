package com.sistem.penjualan.atk.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Pelanggan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPelanggan;

    @NotBlank()
    private String namaPelanggan;

    @NotBlank()
    private String alamat;

    @NotBlank()
    @Pattern(regexp = "\\d+")
    private String telepon;
}