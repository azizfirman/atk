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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.service.PenggunaService;

@RestController
@RequestMapping("/api/pengguna")
public class PenggunaController {

    @Autowired
    private PenggunaService penggunaService;

    @GetMapping
    public List<Pengguna> getAllPengguna() {
        return penggunaService.getAllPengguna();
    }

    @GetMapping("/{idPengguna}")
    public Optional<Pengguna> getPenggunaById(@PathVariable UUID idPengguna) {
        return penggunaService.getPenggunaById(idPengguna);
    }

    @PostMapping
    public Pengguna savePengguna(@RequestPart Pengguna pengguna, @RequestPart MultipartFile photo) {
        return penggunaService.savePengguna(pengguna);
    }

    @PutMapping
    public Pengguna updatePengguna(@RequestBody Pengguna pengguna) {
        return penggunaService.updatePengguna(pengguna);
    }

    @DeleteMapping("/{idPengguna}")
    public void deletePengguna(@PathVariable UUID idPengguna) {
        penggunaService.deletePengguna(idPengguna);
    }
}