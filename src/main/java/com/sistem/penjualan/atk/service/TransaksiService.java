package com.sistem.penjualan.atk.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.penjualan.atk.entity.Transaksi;
import com.sistem.penjualan.atk.repository.TransaksiRepository;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    public List<Transaksi> getAllTransaksi() {
        return transaksiRepository.findAll();
    }

    public Optional<Transaksi> getTransaksiById(UUID idTransaksi) {
        return transaksiRepository.findById(idTransaksi);
    }

    public Transaksi saveTransaksi(Transaksi transaksi) {
        return transaksiRepository.save(transaksi);
    }

    public Transaksi updateTransaksi(Transaksi transaksi) {
        return transaksiRepository.findById(transaksi.getIdTransaksi()).map(data -> {
            Optional.ofNullable(transaksi.getTanggalTranskasi()).ifPresent(data::setTanggalTranskasi);
            Optional.ofNullable(transaksi.getBarang()).ifPresent(data::setBarang);
            Optional.ofNullable(transaksi.getPelanggan()).ifPresent(data::setPelanggan);
            return transaksiRepository.save(data);
        }).orElse(new Transaksi());
    }

    public void deleteTransaksi(UUID idTransaksi) {
        transaksiRepository.deleteById(idTransaksi);
    }
}