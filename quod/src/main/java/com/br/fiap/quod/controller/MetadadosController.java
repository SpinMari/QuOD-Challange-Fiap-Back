package com.br.fiap.quod.controller;


import com.br.fiap.quod.domain.Imagem;
import com.br.fiap.quod.repository.ImagemRepository;
import com.br.fiap.quod.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metadados")
public class MetadadosController {

    private final ImagemRepository imagemRepository;
    private final ImagemService imagemService;

    @Autowired
    public MetadadosController(ImagemRepository imagemRepository, ImagemService imagemService) {
        this.imagemRepository = imagemRepository;
        this.imagemService = imagemService;
    }
    @GetMapping("/biometria/{tipo}")
    public ResponseEntity<List<Imagem>> getByBiometria(@PathVariable String tipo) {
        return ResponseEntity.ok(imagemRepository.findByTipoBiometria(tipo));
    }

    @GetMapping("/fraude")
    public ResponseEntity<List<Imagem>> getByFraude(@RequestParam boolean detectada) {
        return ResponseEntity.ok(imagemRepository.findByFraudeDetectada(detectada));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Imagem>> getByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(imagemRepository.findByDataCapturaBetween(inicio, fim));
    }

    @GetMapping("/dispositivo/{id}")
    public ResponseEntity<List<Imagem>> getByDispositivo(@PathVariable String id) {
        return ResponseEntity.ok(imagemRepository.findByDispositivo_Id(id));
    }

    @DeleteMapping("/limpar")
    public ResponseEntity<Void> limparImagensAntigas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data
    ) {
        imagemService.deletarImagensAntigas(data);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/estatisticas/biometria")
    public ResponseEntity<Map<String, Long>> getEstatisticasBiometria(@RequestParam String tipo) {
        long count = imagemRepository.countByTipoBiometria(tipo);
        return ResponseEntity.ok(Map.of(tipo, count));
    }
}
