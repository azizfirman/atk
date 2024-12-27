package com.sistem.penjualan.atk.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.penjualan.atk.entity.Pelanggan;
import com.sistem.penjualan.atk.service.PelangganService;

@RestController
@RequestMapping("/api/pelanggan")
public class PelangganController {

    @Autowired
    private PelangganService pelangganService;

    @GetMapping
    public List<Pelanggan> getAllPelanggan() {
        return pelangganService.getAllPelanggan();
    }

    @GetMapping("/{idPelanggan}")
    public Optional<Pelanggan> getPelangganById(@PathVariable UUID idPelanggan) {
        return pelangganService.getPelangganById(idPelanggan);
    }

    @PostMapping
    public Pelanggan savePelanggan(@RequestBody Pelanggan pelanggan) {
        return pelangganService.savePelanggan(pelanggan);
    }

    @PutMapping
    public Pelanggan updatePelanggan(@RequestBody Pelanggan pelanggan) {
        return pelangganService.updatePelanggan(pelanggan);
    }

    @DeleteMapping("/{idPelanggan}")
    public void deletePelanggan(@PathVariable UUID idPelanggan) {
        pelangganService.deletePelanggan(idPelanggan);
    }
}