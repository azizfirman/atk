package com.sistem.penjualan.atk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sistem.penjualan.atk.service.PageService;

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
    public String homePage() {
        return "pages/home";
    }

    @GetMapping("/pengguna")
    public String penggunaPage() {
        return "pages/pengguna";
    }

    @GetMapping("/barang")
    public String barangPage() {
        return "pages/barang";
    }

    @GetMapping("/pelanggan")
    public String pelangganPage() {
        return "pages/pelanggan";
    }

    @GetMapping("/transaksi")
    public String transaksiPage() {
        return "pages/transaksi";
    }

    @GetMapping("/laporan")
    public String laporanPage() {
        return "pages/laporan";
    }
}