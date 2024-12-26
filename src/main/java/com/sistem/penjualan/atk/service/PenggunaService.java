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
    private PenggunaRepository penggunaRepository;

    public List<Pengguna> getAllPengguna() {
        return penggunaRepository.findAll();
    }

    public Optional<Pengguna> getPenggunaById(UUID idPengguna) {
        return penggunaRepository.findById(idPengguna);
    }

    public Pengguna savePengguna(Pengguna pengguna) {
        return penggunaRepository.save(pengguna);
    }

    public void deletePengguna(UUID idPengguna) {
        penggunaRepository.deleteById(idPengguna);
    }
}