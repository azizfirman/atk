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

    public static final String PAGE_TITLE = "Sistem Penjualan Alat Tulis Kantor";
}