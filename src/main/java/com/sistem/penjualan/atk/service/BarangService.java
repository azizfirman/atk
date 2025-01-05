package com.sistem.penjualan.atk.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Barang;
import com.sistem.penjualan.atk.repository.BarangRepository;
import com.sistem.penjualan.atk.repository.TransaksiRepository;

@Service
public class BarangService {

    @Autowired
    private BarangRepository barangRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    public Page<Barang> getAllBarang(String cari, Pageable pageable) {
        if(!cari.isEmpty()){
            return searchBarang(cari, pageable);
        }
        return barangRepository.findAll(pageable);
    }

    public Optional<Barang> getBarangById(UUID idBarang) {
        return barangRepository.findById(idBarang);
    }

    public Barang saveBarang(Barang barang) {
        barang.setCreatedAt(LocalDateTime.now());
        return barangRepository.save(barang);
    }

    public Barang updateBarang(Barang barang) {
        return barangRepository.findById(barang.getIdBarang()).map(data -> {
            Optional.ofNullable(barang.getNamaBarang()).ifPresent(data::setNamaBarang);
            Optional.ofNullable(barang.getHargaBarang()).ifPresent(data::setHargaBarang);
            Optional.ofNullable(barang.getStokBarang()).ifPresent(data::setStokBarang);
            Optional.ofNullable(barang.getPelanggan()).ifPresent(data::setPelanggan);
            Optional.ofNullable(barang.getPengguna()).ifPresent(data::setPengguna);
            data.setUpdateAt(LocalDateTime.now());

            return barangRepository.save(data);
        }).orElse(new Barang());
    }

    public String deleteBarang(UUID idBarang) {
        if(transaksiRepository.existsByBarang_IdBarang(idBarang)){
            return "Barang ini tidak dapat dihapus karena ada transaksi yang terkait.";
        }
        else {
            barangRepository.deleteById(idBarang);
        }
        return "";
    }

    public Page<Barang> searchBarang(String cari, Pageable pageable) {
        return barangRepository.findByNamaBarangContainingIgnoreCase(cari, pageable);
    }

    public long getCountByStokBarangGreaterThan(int stokBarang) {
        return barangRepository.countByStokBarangGreaterThan(stokBarang);
    }

    public long getCountByStokBarangLessThanEqual(int stokBarang) {
        return barangRepository.countByStokBarangLessThanEqual(stokBarang);
    }
}