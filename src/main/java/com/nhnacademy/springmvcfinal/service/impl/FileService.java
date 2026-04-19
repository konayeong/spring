package com.nhnacademy.springmvcfinal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public String save(MultipartFile file) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            
            String original = file.getOriginalFilename();
            String ext = original != null ? original.substring(original.lastIndexOf(".")) : null;

            String fileName = UUID.randomUUID() + ext;
            log.debug("[File Save] file name : {}", file.getOriginalFilename());
            File dest = new File(dir, fileName);

            file.transferTo(dest);

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
