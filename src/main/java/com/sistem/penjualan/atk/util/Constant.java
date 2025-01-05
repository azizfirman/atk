package com.sistem.penjualan.atk.util;

import org.springframework.stereotype.Component;

@Component()
public class Constant {

    public static class Security {
        public static String[] permitAll() {
            return new String[] {
                "/", "/css/**", "/js/**", "/img/**", "/login",
                "/swagger-ui.html", "/swagger-ui/**", "/api-docs/**"
            };
        }
    }

    public static class Text {
        public static final String TITLE = "Sistem Penjualan Alat Tulis Kantor";
        public static final String LOGIN = "Login";
        public static final String HINT_USERNAME = "Masukkan username Anda";
        public static final String HINT_PASSWORD = "Masukkan password Anda";
        public static final String ERROR_LOGIN = "Username atau password salah!";
        public static final String BERANDA = "Beranda";
        public static final String BARANG = "Barang";
        public static final String TRANSAKSI = "Transaksi";
        public static final String PELANGGAN = "Pelanggan";
        public static final String LAPORAN = "Laporan";
        public static final String KELOLA_PENGGUNA = "Kelola Pengguna";
        public static final String LOGOUT = "Logout";
        public static final String TAMBAH_BARANG = "Tambah Barang";
        public static final String NAMA_BARANG = "Nama Barang";
        public static final String HARGA_BARANG = "Harga Barang";
        public static final String STOK_BARANG = "Stok Barang";
        public static final String UPLOAD = "Upload";
        public static final String SIMPAN = "Simpan";
        public static final String PROFIL = "Profil";
        public static final String CARI = "Cari";
        public static final String TAMBAH_PENGGUNA = "Tambah Pengguna";
        public static final String EDIT_PENGGUNA = "Edit Pengguna";
        public static final String RESET_PASSWORD = "Reset Password";
        public static final String GANTI_ROLE = "Ganti Role";
        public static final String HAPUS_AKUN = "Hapus Akun";
    }
}