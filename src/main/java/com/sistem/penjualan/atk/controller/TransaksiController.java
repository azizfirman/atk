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

import com.sistem.penjualan.atk.entity.Transaksi;
import com.sistem.penjualan.atk.service.TransaksiService;

@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {

    @Autowired
    private TransaksiService transaksiService;

    @GetMapping
    public List<Transaksi> getAllTransaksi() {
        return transaksiService.getAllTransaksi();
    }

    @GetMapping("/{idTransaksi}")
    public Optional<Transaksi> getTransaksiById(@PathVariable UUID idTransaksi) {
        return transaksiService.getTransaksiById(idTransaksi);
    }

    @PostMapping
    public Transaksi saveTransaksi(@RequestBody Transaksi transaksi) {
        return transaksiService.saveTransaksi(transaksi);
    }

    @PutMapping
    public Transaksi updateTransaksi(@RequestBody Transaksi transaksi) {
        return transaksiService.updateTransaksi(transaksi);
    }

    @DeleteMapping("/{idTransaksi}")
    public void deleteTransaksi(@PathVariable UUID idTransaksi) {
        transaksiService.deleteTransaksi(idTransaksi);
    }
}