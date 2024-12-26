package com.sistem.penjualan.atk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String splashPage() {
        return "splash";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
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