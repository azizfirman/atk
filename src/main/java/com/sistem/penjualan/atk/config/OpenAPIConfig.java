package com.sistem.penjualan.atk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sistem.penjualan.atk.util.Constant;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(
            new Info().title(Constant.Text.TITLE)
                .description("Ini adalah project ".concat(Constant.Text.TITLE))
                .contact(new Contact().name("Firman").email("aziz.firman@prosia.co.id"))
        );
    }
}