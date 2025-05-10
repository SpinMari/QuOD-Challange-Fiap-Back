package com.br.fiap.quod.controller;

import com.br.fiap.quod.dto.DispositivoDTO;
import com.br.fiap.quod.service.DispositivoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @GetMapping
    public ResponseEntity<List<DispositivoDTO>> getAllDispositivos() {
        return ResponseEntity.ok(dispositivoService.getAll());
    }

    @GetMapping("/modelo/{modelo}")
    public ResponseEntity<DispositivoDTO> getByModelo(@PathVariable String modelo) {
        return ResponseEntity.ok(dispositivoService.getByModelo(modelo));
    }

    @GetMapping("/fabricante/{fabricante}")
    public ResponseEntity<List<DispositivoDTO>> getByFabricante(@PathVariable String fabricante) {
        return ResponseEntity.ok(dispositivoService.getByFabricante(fabricante));
    }

    @GetMapping("/fabricante/{fabricante}/modelo/{modelo}")
    public ResponseEntity<List<DispositivoDTO>> getByFabricanteAndModelo(@PathVariable String fabricante, @PathVariable String modelo) {
        return ResponseEntity.ok(dispositivoService.getByFabricanteAndModelo(fabricante, modelo));
    }
}