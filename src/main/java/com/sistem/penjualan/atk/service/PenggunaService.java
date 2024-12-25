package com.sistem.penjualan.atk.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.repository.PenggunaRepository;

@Service
public class PenggunaService {

    @Autowired
    PenggunaRepository penggunaRepository;

    public PenggunaService(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
    }

    public List<Pengguna> getAllPengguna() {
        return penggunaRepository.findAll();
    }

    public Optional<Pengguna> getPenggunaById(UUID id) {
        return penggunaRepository.findById(id);
    }

    public Pengguna createPengguna(Pengguna pengguna) {
        return penggunaRepository.save(pengguna);
    }

    public Pengguna updatePengguna(UUID id, Pengguna pengguna) {
        Optional<Pengguna> existingPengguna = penggunaRepository.findById(id);
        if (existingPengguna.isPresent()) {
            Pengguna updatedPengguna = existingPengguna.get();
            updatedPengguna.setNamaPengguna(pengguna.getNamaPengguna());
            updatedPengguna.setUsername(pengguna.getUsername());
            updatedPengguna.setPassword(pengguna.getPassword());
            updatedPengguna.setLevel(pengguna.getLevel());
            return penggunaRepository.save(updatedPengguna);
        } else {
            throw new RuntimeException("Pengguna dengan ID " + id + " tidak ditemukan!");
        }
    }

    public void deletePengguna(UUID id) {
        penggunaRepository.deleteById(id);
    }
}