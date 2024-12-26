package com.sistem.penjualan.atk.entity;

import java.util.UUID;

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
    private UUID idPengguna;

    private String namaPengguna;
    private String username;
    private String password;
    private String level;
}