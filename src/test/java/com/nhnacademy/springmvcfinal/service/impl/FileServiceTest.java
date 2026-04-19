package com.nhnacademy.springmvcfinal.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private final FileService fileService = new FileService();

    @Test
    @DisplayName("파일 저장 성공")
    void save_success() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "hello".getBytes());

        String savedFileName = fileService.save(file);

        assertNotNull(savedFileName);
        assertTrue(savedFileName.endsWith(".jpg"));

        File saved = new File(System.getProperty("user.dir") + "/uploads/" + savedFileName);
        assertTrue(saved.exists());

        saved.delete();
    }
}