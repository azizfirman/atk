package com.sistem.penjualan.atk.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = true)
    private String telepon;

    @NotNull
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updateAt;
}