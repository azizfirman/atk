package com.sistem.penjualan.atk.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.sistem.penjualan.atk.entity.Barang;
import com.sistem.penjualan.atk.entity.Pelanggan;
import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.entity.Transaksi;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PageService {

    @Autowired
    private PenggunaService penggunaService;

    @Autowired
    private BarangService barangService;

    @Autowired
    private PelangganService pelangganService;

    @Autowired
    private TransaksiService transaksiService;

    public String getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/home";
        }
        return "pages/login";
    }

    public String getHome(Model model) {
        Map<String, Object> productSalesProportion = transaksiService.getProductSalesProportion();
        List<Integer> transaksiCurrentMonth = transaksiService.getTransaksiByCurrentMonth();
        List<Integer> transaksiCurrentWeek = transaksiService.getTransaksiByCurrentWeek();

        long warningBarang = barangService.getCountByStokBarangLessThanEqual(5);
        long totalBarang = barangService.getCountByStokBarangGreaterThan(0);
        long totalTransaksi = transaksiService.getCountTotalTransaksiToday();
        long totalPelanggan = pelangganService.getTotalPelanggan();
        long totalPengguna = penggunaService.getTotalPengguna();

        model.addAttribute("productSalesProportion", productSalesProportion);
        model.addAttribute("transaksiCurrentMonth", transaksiCurrentMonth);
        model.addAttribute("transaksiCurrentWeek", transaksiCurrentWeek);
        model.addAttribute("totalPelanggan", totalPelanggan);
        model.addAttribute("totalTransaksi", totalTransaksi);
        model.addAttribute("warningBarang", warningBarang);
        model.addAttribute("totalPengguna", totalPengguna);
        model.addAttribute("totalBarang", totalBarang);
        model.addAttribute("path", "/home");
        return "pages/home";
    }

    public String getBarang(Model model, String cari, Pageable pageable) {
        if(cari.isEmpty()){
            Page<Barang> barangPage = barangService.getAllBarang(cari, pageable);

            model.addAttribute("dataBarang", barangPage);
            model.addAttribute("currentPage", barangPage.getNumber());
            model.addAttribute("totalPages", barangPage.getTotalPages());
            model.addAttribute("totalData", barangPage.getTotalElements());
        }
        else {
            Page<Barang> barangPage = barangService.searchBarang(cari, pageable);

            model.addAttribute("dataBarang", barangPage);
            model.addAttribute("currentPage", barangPage.getNumber());
            model.addAttribute("totalPages", barangPage.getTotalPages());
            model.addAttribute("totalData", barangPage.getTotalElements());
        }
        model.addAttribute("path", "/barang");
        return "pages/home";
    }

    public String getTransaksi(Model model, String cari, Pageable pageable) {
        if(cari.isEmpty()){
            Page<Map<String, Object>> transaksiPage = transaksiService.getAllTransaksi(pageable);

            model.addAttribute("dataTransaksi", transaksiPage.getContent());
            model.addAttribute("currentPage", transaksiPage.getNumber());
            model.addAttribute("totalPages", transaksiPage.getTotalPages());
            model.addAttribute("totalData", transaksiPage.getTotalElements());
        }
        else {
            Page<Map<String, Object>> transaksiPage = transaksiService.searchTransaksi(cari, pageable);

            model.addAttribute("dataTransaksi", transaksiPage.getContent());
            model.addAttribute("currentPage", transaksiPage.getNumber());
            model.addAttribute("totalPages", transaksiPage.getTotalPages());
            model.addAttribute("totalData", transaksiPage.getTotalElements());
        }
        model.addAttribute("path", "/transaksi");
        return "pages/home";
    }

    public String getPelanggan(Model model, String cari, Pageable pageable) {
        if(cari.isEmpty()){
            Page<Pelanggan> pelangganPage = pelangganService.getAllPelanggan(cari, pageable);

            model.addAttribute("dataPelanggan", pelangganPage);
            model.addAttribute("currentPage", pelangganPage.getNumber());
            model.addAttribute("totalPages", pelangganPage.getTotalPages());
            model.addAttribute("totalData", pelangganPage.getTotalElements());
        }
        else {
            Page<Pelanggan> pelangganPage = pelangganService.searchPelanggan(cari, pageable);

            model.addAttribute("dataPelanggan", pelangganPage);
            model.addAttribute("currentPage", pelangganPage.getNumber());
            model.addAttribute("totalPages", pelangganPage.getTotalPages());
            model.addAttribute("totalData", pelangganPage.getTotalElements());
        }
        model.addAttribute("path", "/pelanggan");
        return "pages/home";
    }

    public Object getLaporan(Model model, String cari, String export, Pageable pageable, HttpServletResponse response) {
        if(cari.isEmpty()){
            Page<Transaksi> transaksiPage = transaksiService.getAllLaporanTransaksi(pageable);
            Double totalSales = transaksiService.getCountTotalSales(cari, transaksiPage.getContent());

            model.addAttribute("totalSales", totalSales);
            model.addAttribute("dataLaporan", transaksiPage);
            model.addAttribute("currentPage", transaksiPage.getNumber());
            model.addAttribute("totalPages", transaksiPage.getTotalPages());
            model.addAttribute("totalData", transaksiPage.getTotalElements());

            if(!export.isEmpty()){
                List<Transaksi> data = transaksiService.getAllLaporanTransaksi(null).getContent();
                transaksiService.generateExport(export, response, data);
                return ResponseEntity.ok().build();
            }
        }
        else {
            String[] parameters = cari.split(",");
            LocalDate startDate = null;
            LocalDate endDate = null;
            String namaBarang = "";
            String namaPelanggan = "";
            String namaPengguna = "";

            for(String parameter : parameters) {
                String[] keyValue = parameter.split(":");
                if(keyValue.length == 2) {
                    switch (keyValue[0].trim()) {
                        case "barang":
                            namaBarang = keyValue[1].trim();
                            break;
                        case "pelanggan":
                            namaPelanggan = keyValue[1].trim();
                            break;
                        case "pengguna":
                            namaPengguna = keyValue[1].trim();
                            break;
                        case "tanggal":
                            String[] dates = keyValue[1].trim().split("-");
                            if(dates.length == 2) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                startDate = LocalDate.parse(dates[0].trim(), formatter);
                                endDate = LocalDate.parse(dates[1].trim(), formatter);
                            }
                            break;
                    }
                }
            }

            Page<Transaksi> transaksiPage = transaksiService.searchLaporanTransaksi(startDate, endDate, namaBarang, namaPelanggan, namaPengguna, pageable);
            Page<Transaksi> transaksiAllPage = transaksiService.searchLaporanTransaksi(startDate, endDate, namaBarang, namaPelanggan, namaPengguna, null);
            Double totalSales = transaksiService.getCountTotalSales(cari, transaksiAllPage.getContent());

            model.addAttribute("totalSales", totalSales);
            model.addAttribute("dataLaporan", transaksiPage);
            model.addAttribute("currentPage", transaksiPage.getNumber());
            model.addAttribute("totalPages", transaksiPage.getTotalPages());
            model.addAttribute("totalData", transaksiPage.getTotalElements());

            if(!export.isEmpty()){
                transaksiService.generateExport(export, response, transaksiAllPage.getContent());
                return ResponseEntity.ok().build();
            }
        }
        model.addAttribute("path", "/laporan");
        return "pages/home";
    }

    public String getProfil(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pengguna pengguna = penggunaService.getPenggunabyUsername(username);
        List<Integer> listTransaksi = transaksiService.getTransaksiByPenggunaAndCurrentMonth(pengguna.getIdPengguna());

        model.addAttribute("dataTransaksi", listTransaksi);
        model.addAttribute("path", "/profil");
        return "pages/home";
    }

    public String getPengguna(Model model, String cari, Pageable pageable) {
        if(cari.isEmpty()){
            model.addAttribute("dataPengguna", penggunaService.getAllPengguna(cari, pageable));
        }
        else {
            model.addAttribute("dataPengguna", penggunaService.searchUsers(cari));
        }
        model.addAttribute("path", "/pengguna");
        return "pages/home";
    }

    public String getError(Exception exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "pages/404";
    }
}