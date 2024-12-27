package com.sistem.penjualan.atk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sistem.penjualan.atk.entity.Pengguna;
import com.sistem.penjualan.atk.repository.PenggunaRepository;
import com.sistem.penjualan.atk.util.Constant;

@ControllerAdvice
public class AdviceController {

    @Autowired
    private PenggunaRepository penggunaRepository;

    @ModelAttribute
    public void addAttribute(Model model) {
        model.addAttribute("title", Constant.Text.TITLE);
        model.addAttribute("login", Constant.Text.LOGIN);
        model.addAttribute("hintUsername", Constant.Text.HINT_USERNAME);
        model.addAttribute("hintPassword", Constant.Text.HINT_PASSWORD);
        model.addAttribute("errorLogin", Constant.Text.ERROR_LOGIN);
        model.addAttribute("beranda", Constant.Text.BERANDA);
        model.addAttribute("transaksi", Constant.Text.TRANSAKSI);
        model.addAttribute("pelanggan", Constant.Text.PELANGGAN);
        model.addAttribute("cariBarang", Constant.Text.CARI_BARANG);
        model.addAttribute("aturPengguna", Constant.Text.ATUR_PENGGUNA);
        model.addAttribute("logout", Constant.Text.LOGOUT);
        model.addAttribute("tambahBarang", Constant.Text.TAMBAH_BARANG);
        model.addAttribute("namaBarang", Constant.Text.NAMA_BARANG);
        model.addAttribute("hargaBarang", Constant.Text.HARGA_BARANG);
        model.addAttribute("stokBarang", Constant.Text.STOK_BARANG);
        model.addAttribute("fotoBarang", Constant.Text.FOTO_BARANG);
        model.addAttribute("upload", Constant.Text.UPLOAD);
        model.addAttribute("simpan", Constant.Text.SIMPAN);
    }

    @ModelAttribute
    public void addUserDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username     = authentication.getName();
            Pengguna pengguna   = penggunaRepository.findByUsername(username).get();

            if(pengguna != null) {
                model.addAttribute("photo", pengguna.getPhoto());
                model.addAttribute("username", pengguna.getUsername());
                model.addAttribute("namaPengguna", pengguna.getNamaPengguna());
                model.addAttribute("isAdmin", pengguna.getLevel().equals("ADMIN"));
            }
        }
    }
}