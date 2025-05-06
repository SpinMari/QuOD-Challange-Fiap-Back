package com.br.fiap.quod.service;

import org.springframework.stereotype.Service;

@Service
public class FileStorageService {
    
    // Configure storage location
    
    public String storeFile(String base64Image, String filename) {
        // Decode base64 and save to storage
        // Return storage path or URL
        return base64Image;
    }
}