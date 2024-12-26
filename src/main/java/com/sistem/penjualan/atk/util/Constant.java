package com.sistem.penjualan.atk.util;

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
    }
}