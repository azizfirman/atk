package com.sistem.penjualan.atk.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Pelanggan;
import com.sistem.penjualan.atk.repository.PelangganRepository;
import com.sistem.penjualan.atk.repository.TransaksiRepository;

@Service
public class PelangganService {

    @Autowired
    private PelangganRepository pelangganRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    public Page<Pelanggan> getAllPelanggan(String cari, Pageable pageable) {
        if(!cari.isEmpty()){
            return searchPelanggan(cari, pageable);
        }
        return pelangganRepository.findAll(pageable);
    }

    public Optional<Pelanggan> getPelangganById(UUID idPelanggan) {
        return pelangganRepository.findById(idPelanggan);
    }

    public Pelanggan savePelanggan(Pelanggan pelanggan) {
        pelanggan.setCreatedAt(LocalDateTime.now());
        return pelangganRepository.save(pelanggan);
    }

    public Pelanggan updatePelanggan(Pelanggan pelanggan) {
        return pelangganRepository.findById(pelanggan.getIdPelanggan()).map(data -> {
            Optional.ofNullable(pelanggan.getNamaPelanggan()).ifPresent(data::setNamaPelanggan);
            Optional.ofNullable(pelanggan.getTelepon()).ifPresent(data::setTelepon);
            Optional.ofNullable(pelanggan.getAlamat()).ifPresent(data::setAlamat);
            data.setUpdateAt(LocalDateTime.now());

            return pelangganRepository.save(data);
        }).orElse(new Pelanggan());
    }

    public String deletePelanggan(UUID idPelanggan) {
        if(transaksiRepository.existsByPelanggan_IdPelanggan(idPelanggan)){
            return "Pelanggan ini tidak dapat dihapus karena ada transaksi yang terkait.";
        }
        else {
            pelangganRepository.deleteById(idPelanggan);
        }
        return "";
    }

    public Page<Pelanggan> searchPelanggan(String cari, Pageable pageable) {
        return pelangganRepository.findByNamaPelangganContainingIgnoreCaseOrAlamatContainingIgnoreCaseOrTeleponContainingIgnoreCase(cari, cari, cari, pageable);
    }

    public long getTotalPelanggan() {
        return pelangganRepository.count();
    }
}