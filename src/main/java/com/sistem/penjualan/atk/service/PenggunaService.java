package com.sistem.penjualan.atk.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.repository.PenggunaRepository;
import com.sistem.penjualan.atk.repository.TransaksiRepository;

@Service
public class PenggunaService {

    @Autowired
    private PenggunaRepository penggunaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TransaksiRepository transaksiRepository;

    public List<Pengguna> getAllPengguna(String cari, Pageable pageable) {
        if(!cari.isEmpty()){
            return searchUsersByNamaPengguna(cari, pageable).getContent();
        }
        return penggunaRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Optional<Pengguna> getPenggunaById(UUID idPengguna) {
        return penggunaRepository.findById(idPengguna);
    }

    public Pengguna savePengguna(Pengguna pengguna, MultipartFile photo) {
        String encodedPassword = passwordEncoder.encode(pengguna.getPassword());
        pengguna.setPassword(encodedPassword);
        pengguna.setCreatedAt(LocalDateTime.now());

        try {
            if(!Objects.isNull(photo) && Objects.nonNull(photo.getOriginalFilename())){
                String oriFileName = photo.getOriginalFilename();

                if(Objects.nonNull(oriFileName)){
                    String timestamp    = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String extension    = oriFileName.substring(oriFileName.lastIndexOf('.'));
                    String uniqueName   = timestamp.concat(extension);
                    Path filePath       = Paths.get("src/main/resources/static/img/photos/" + uniqueName);

                    Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    pengguna.setPhoto("/img/photos/" + uniqueName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Pengguna();
        }

        return penggunaRepository.save(pengguna);
    }

    public Pengguna updatePengguna(Pengguna pengguna, MultipartFile photo) {
        try {
            if(Objects.nonNull(pengguna.getPassword())){
                String encodedPassword = passwordEncoder.encode(pengguna.getPassword());
                pengguna.setPassword(encodedPassword);
            }

            if(!Objects.isNull(photo) && Objects.nonNull(photo.getOriginalFilename())){
                Pengguna dataPengguna = penggunaRepository.findById(pengguna.getIdPengguna()).get();
                Path oldFilePath = Paths.get("src/main/resources/static" + dataPengguna.getPhoto());

                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath); // Hapus file
                }

                String oriFileName = photo.getOriginalFilename();
                if(Objects.nonNull(oriFileName)){
                    String timestamp    = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String extension    = oriFileName.substring(oriFileName.lastIndexOf('.'));
                    String uniqueName   = timestamp.concat(extension);
                    Path filePath       = Paths.get("src/main/resources/static/img/photos/" + uniqueName);

                    Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    pengguna.setPhoto("/img/photos/" + uniqueName);
                }
            }

            Pengguna dataPengguna = penggunaRepository.findById(pengguna.getIdPengguna()).map(data -> {
                Optional.ofNullable(pengguna.getPassword()).ifPresent(data::setPassword);
                Optional.ofNullable(pengguna.getNamaPengguna()).ifPresent(data::setNamaPengguna);
                Optional.ofNullable(pengguna.getPhoto()).ifPresent(data::setPhoto);
                Optional.ofNullable(pengguna.getLevel()).ifPresent(data::setLevel);

                data.setUpdateAt(LocalDateTime.now());
                return data;
            }).orElse(new Pengguna());

            return penggunaRepository.save(dataPengguna);
        } catch (IOException e) {
            e.printStackTrace();
            return new Pengguna();
        }
    }

    public String deletePengguna(UUID idPengguna) {
        if(transaksiRepository.existsByPengguna_IdPengguna(idPengguna)){
            return "Pengguna ini tidak dapat dihapus karena ada transaksi yang terkait.";
        }
        else {
            try {
                Pengguna pengguna = penggunaRepository.findById(idPengguna).get();
                // Lokasi file
                Path filePath = Paths.get("src/main/resources/static" + pengguna.getPhoto());
                if (Files.exists(filePath)) {
                    Files.delete(filePath); // Hapus file
                }
                penggunaRepository.deleteById(idPengguna);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public boolean isUsernameTaken(String username) {
        return penggunaRepository.existsByUsername(username);
    }

    public List<Pengguna> searchUsers(String cari) {
        return penggunaRepository.findByNamaPenggunaContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrLevelContainingIgnoreCase(cari, cari, cari);
    }

    public Pengguna getPenggunabyUsername(String username) {
        return penggunaRepository.findByUsername(username).orElse(null);
    }

    public long getTotalPengguna() {
        return penggunaRepository.count();
    }

    public Page<Pengguna> searchUsersByNamaPengguna(String cari, Pageable pageable) {
        return penggunaRepository.findByNamaPenggunaContainingIgnoreCase(cari, pageable);
    }
}