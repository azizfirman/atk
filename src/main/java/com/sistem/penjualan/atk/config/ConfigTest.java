package com.sistem.penjualan.atk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class ConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigTest.class);

    @Value("${DATABASE_HOST}")
    private String databaseHost;

    @Value("${DATABASE_PORT}")
    private String databasePort;

    @Value("${DATABASE_NAME}")
    private String databaseName;

    @Value("${DATABASE_USERNAME}")
    private String databaseUsername;

    @Value("${DATABASE_PASSWORD}")
    private String databasePassword;

    @PostConstruct
    public void init() {
        logger.info("Database Host: {}", databaseHost);
        logger.info("Database Port: {}", databasePort);
        logger.info("Database Name: {}", databaseName);
        logger.info("Database Username: {}", databaseUsername);
    }
}