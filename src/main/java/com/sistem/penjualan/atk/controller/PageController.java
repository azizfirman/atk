package com.sistem.penjualan.atk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sistem.penjualan.atk.service.PageService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("/")
    public String splashPage() {
        return "pages/splash";
    }

    @GetMapping("/login")
    public String loginPage() {
        return pageService.getLogin();
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return pageService.getHome(model);
    }

    @GetMapping("/barang")
    public String barangPage(Model model, @RequestParam(required = false, defaultValue = "") String cari, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return pageService.getBarang(model, cari, pageable);
    }

    @GetMapping("/transaksi")
    public String transaksiPage(Model model, @RequestParam(required = false, defaultValue = "") String cari, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return pageService.getTransaksi(model, cari, pageable);
    }

    @GetMapping("/pelanggan")
    public String pelangganPage(Model model, @RequestParam(required = false, defaultValue = "") String cari, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return pageService.getPelanggan(model, cari, pageable);
    }

    @GetMapping("/laporan")
    public Object laporanPage(Model model, @RequestParam(required = false, defaultValue = "") String cari, @RequestParam(required = false, defaultValue = "") String export, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, HttpServletResponse response) {
        return pageService.getLaporan(model, cari, export, pageable, response);
    }

    @GetMapping("/profil")
    public String profilPage(Model model) {
        return pageService.getProfil(model);
    }

    @GetMapping("/pengguna")
    public String penggunaPage(Model model, @RequestParam(required = false, defaultValue = "") String cari, Pageable pageable) {
        return pageService.getPengguna(model, cari, pageable);
    }
}