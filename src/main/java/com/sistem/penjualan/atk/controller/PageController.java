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

    @GetMapping("/pengguna")
    public String penggunaPage() {
        return "pengguna";
    }

    @GetMapping("/barang")
    public String barangPage() {
        return "barang";
    }

    @GetMapping("/pelanggan")
    public String pelangganPage() {
        return "pelanggan";
    }

    @GetMapping("/transaksi")
    public String transaksiPage() {
        return "transaksi";
    }

    @GetMapping("/laporan")
    public String laporanPage() {
        return "laporan";
    }
}