package com.sistem.penjualan.atk.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<Pengguna> getAllPengguna(@RequestParam(required = false, defaultValue = "") String cari, Pageable pageable) {
        return penggunaService.getAllPengguna(cari, pageable);
    }

    @GetMapping("/{idPengguna}")
    public Optional<Pengguna> getPenggunaById(@PathVariable UUID idPengguna) {
        return penggunaService.getPenggunaById(idPengguna);
    }

    @PostMapping
    public Pengguna savePengguna(@RequestPart Pengguna pengguna, @RequestPart(required = false) MultipartFile photo) {
        return penggunaService.savePengguna(pengguna, photo);
    }

    @PutMapping
    public Pengguna updatePengguna(@RequestPart Pengguna pengguna, @RequestPart(required = false) MultipartFile photo) {
        return penggunaService.updatePengguna(pengguna, photo);
    }

    @DeleteMapping("/{idPengguna}")
    public String deletePengguna(@PathVariable UUID idPengguna) {
        return penggunaService.deletePengguna(idPengguna);
    }

    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        return penggunaService.isUsernameTaken(username);
    }
}