package com.example.didyouknow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Bean
    public Path fileStorageLocation() {
        Path uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
        return uploadPath;
    }
} 