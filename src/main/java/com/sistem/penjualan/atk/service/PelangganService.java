package com.sistem.penjualan.atk.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Pelanggan;
import com.sistem.penjualan.atk.repository.PelangganRepository;

@Service
public class PelangganService {

    @Autowired
    private PelangganRepository pelangganRepository;

    public List<Pelanggan> getAllPelanggan() {
        return pelangganRepository.findAll();
    }

    public Optional<Pelanggan> getPelangganById(UUID idPelanggan) {
        return pelangganRepository.findById(idPelanggan);
    }

    public Pelanggan savePelanggan(Pelanggan pelanggan) {
        return pelangganRepository.save(pelanggan);
    }

    public Pelanggan updatePelanggan(Pelanggan pelanggan) {
        return pelangganRepository.findById(pelanggan.getIdPelanggan()).map(data -> {
            Optional.ofNullable(pelanggan.getNamaPelanggan()).ifPresent(data::setNamaPelanggan);
            Optional.ofNullable(pelanggan.getTelepon()).ifPresent(data::setTelepon);
            Optional.ofNullable(pelanggan.getAlamat()).ifPresent(data::setAlamat);
            return pelangganRepository.save(data);
        }).orElse(new Pelanggan());
    }

    public void deletePelanggan(UUID idPelanggan) {
        pelangganRepository.deleteById(idPelanggan);
    }
}