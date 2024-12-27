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

    public Pengguna updatePengguna(Pengguna pengguna) {
        return penggunaRepository.findById(pengguna.getIdPengguna()).map(data -> {
            Optional.ofNullable(pengguna.getNamaPengguna()).ifPresent(data::setNamaPengguna);
            Optional.ofNullable(pengguna.getPassword()).ifPresent(data::setPassword);
            Optional.ofNullable(pengguna.getPhoto()).ifPresent(data::setPhoto);
            return penggunaRepository.save(data);
        }).orElse(new Pengguna());
    }

    public void deletePengguna(UUID idPengguna) {
        penggunaRepository.deleteById(idPengguna);
    }
}