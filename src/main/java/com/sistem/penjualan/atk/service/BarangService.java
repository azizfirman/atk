package com.sistem.penjualan.atk.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Barang;
import com.sistem.penjualan.atk.repository.BarangRepository;

@Service
public class BarangService {

    @Autowired
    private BarangRepository barangRepository;

    public List<Barang> getAllBarang() {
        return barangRepository.findAll();
    }

    public Optional<Barang> getBarangById(UUID idBarang) {
        return barangRepository.findById(idBarang);
    }

    public Barang saveBarang(Barang barang) {
        return barangRepository.save(barang);
    }

    public Barang updateBarang(Barang barang) {
        return barangRepository.findById(barang.getIdBarang()).map(data -> {
            Optional.ofNullable(barang.getNamaBarang()).ifPresent(data::setNamaBarang);
            Optional.ofNullable(barang.getHargaBarang()).ifPresent(data::setHargaBarang);
            Optional.ofNullable(barang.getStokBarang()).ifPresent(data::setStokBarang);
            Optional.ofNullable(barang.getPelanggan()).ifPresent(data::setPelanggan);
            Optional.ofNullable(barang.getPengguna()).ifPresent(data::setPengguna);
            return barangRepository.save(data);
        }).orElse(new Barang());
    }

    public void deleteBarang(UUID idBarang) {
        barangRepository.deleteById(idBarang);
    }
}