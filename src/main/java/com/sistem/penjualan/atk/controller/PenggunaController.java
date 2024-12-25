package com.sistem.penjualan.atk.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.service.PenggunaService;

@RestController
@RequestMapping("/api/pengguna")
public class PenggunaController {

    @Autowired
    PenggunaService penggunaService;

    public PenggunaController(PenggunaService penggunaService) {
        this.penggunaService = penggunaService;
    }

    @GetMapping
    public ResponseEntity<List<Pengguna>> getAllPengguna() {
        List<Pengguna> penggunaList = penggunaService.getAllPengguna();
        return ResponseEntity.ok(penggunaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pengguna> getPenggunaById(@PathVariable UUID id) {
        Optional<Pengguna> pengguna = penggunaService.getPenggunaById(id);
        return pengguna.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pengguna> createPengguna(@RequestBody Pengguna pengguna) {
        Pengguna createdPengguna = penggunaService.createPengguna(pengguna);
        return ResponseEntity.ok(createdPengguna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pengguna> updatePengguna(@PathVariable UUID id, @RequestBody Pengguna pengguna) {
        try {
            Pengguna updatedPengguna = penggunaService.updatePengguna(id, pengguna);
            return ResponseEntity.ok(updatedPengguna);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePengguna(@PathVariable UUID id) {
        penggunaService.deletePengguna(id);
        return ResponseEntity.noContent().build();
    }
}