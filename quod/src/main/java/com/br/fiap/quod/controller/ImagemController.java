package com.br.fiap.quod.controller;


import com.br.fiap.quod.dto.request.ImagemUploadRequestDTO;
import com.br.fiap.quod.dto.response.ImagemUploadResponseDTO;
import com.br.fiap.quod.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imagem")
public class ImagemController {
    
    private final ImagemService imagemService;
    
    @Autowired
    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<ImagemUploadResponseDTO> uploadImagem(@RequestBody ImagemUploadRequestDTO request) {
        ImagemUploadResponseDTO response = imagemService.uploadImagem(request);
        return ResponseEntity.ok(response);
    }
}