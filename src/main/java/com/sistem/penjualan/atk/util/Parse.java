package com.sistem.penjualan.atk.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class Parse {

    public String toJSON(Object transaksi) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.writeValueAsString(transaksi);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}