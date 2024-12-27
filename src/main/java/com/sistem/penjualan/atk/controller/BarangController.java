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

import com.sistem.penjualan.atk.entity.Barang;
import com.sistem.penjualan.atk.service.BarangService;

@RestController
@RequestMapping("/api/barang")
public class BarangController {

    @Autowired
    private BarangService barangService;

    @GetMapping
    public List<Barang> getAllBarang() {
        return barangService.getAllBarang();
    }

    @GetMapping("/{idBarang}")
    public Optional<Barang> getBarangById(@PathVariable UUID idBarang) {
        return barangService.getBarangById(idBarang);
    }

    @PostMapping
    public Barang saveBarang(@RequestBody Barang barang) {
        return barangService.saveBarang(barang);
    }

    @PutMapping
    public Barang updateBarang(@RequestBody Barang barang) {
        return barangService.updateBarang(barang);
    }

    @DeleteMapping("/{idPengguna}")
    public void deleteBarang(@PathVariable UUID idBarang) {
        barangService.deleteBarang(idBarang);
    }
}