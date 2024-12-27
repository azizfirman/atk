package com.sistem.penjualan.atk.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Barang saveBarang(Barang barang, MultipartFile photoBarang) {
        System.out.println(barang.toString());
        try {
            System.out.println(photoBarang.getOriginalFilename());
            if(!photoBarang.isEmpty() && Objects.nonNull(photoBarang.getOriginalFilename())){
                String oriFileName = photoBarang.getOriginalFilename();

                if(Objects.nonNull(oriFileName)){
                    String timestamp    = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String extension    = oriFileName.substring(oriFileName.lastIndexOf('.'));
                    String uniqueName   = timestamp.concat(extension);
                    Path filePath       = Paths.get("src/main/resources/static/img/barang/" + uniqueName);

                    Files.copy(photoBarang.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    barang.setPhoto(uniqueName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Barang();
        }
        return barangRepository.save(barang);
    }

    public Barang updateBarang(Barang barang) {
        return barangRepository.findById(barang.getIdBarang()).map(data -> {
            Optional.ofNullable(barang.getNamaBarang()).ifPresent(data::setNamaBarang);
            Optional.ofNullable(barang.getHargaBarang()).ifPresent(data::setHargaBarang);
            Optional.ofNullable(barang.getStok()).ifPresent(data::setStok);

            return barangRepository.save(data);
        }).orElse(new Barang());
    }

    public void deleteBarang(UUID idBarang) {
        barangRepository.deleteById(idBarang);
    }
}