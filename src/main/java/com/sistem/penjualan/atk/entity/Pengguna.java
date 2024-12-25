package com.sistem.penjualan.atk.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Pengguna {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pengguna", updatable = false, nullable = false, unique = true)
    private UUID idPengguna;

    @Column(name = "nama_pengguna", nullable = false)
    private String namaPengguna;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "level", nullable = false)
    private String level;
}