package com.example.didyouknow.service;

import com.example.didyouknow.common.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(Path fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("파일 업로드 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // 원본 파일명 정리
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        // 파일명이 null이거나 비어있는 경우 처리
        if (originalFileName == null || originalFileName.isEmpty()) {
            originalFileName = "unknown_file";
        }
        
        // 파일명 유효성 검사
        if (originalFileName.contains("..")) {
            throw new FileStorageException("파일명에 잘못된 경로 문자가 포함되어 있습니다: " + originalFileName);
        }

        // 중복 방지를 위한 고유 파일명 생성
        String fileExtension = "";
        int lastIndex = originalFileName.lastIndexOf(".");
        if (lastIndex > 0) {
            fileExtension = originalFileName.substring(lastIndex);
        } else {
            // 확장자가 없는 경우 기본 확장자 추가
            fileExtension = ".jpg";
        }
        
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // 파일 저장
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일 URL 생성
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(uniqueFileName)
                    .toUriString();

            return fileUrl;
        } catch (IOException ex) {
            throw new FileStorageException("파일 저장 중 오류가 발생했습니다: " + originalFileName, ex);
        }
    }
} 