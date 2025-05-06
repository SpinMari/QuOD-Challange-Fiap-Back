package com.br.fiap.quod.controller;

import com.br.fiap.quod.dto.DispositivoDTO;
import com.br.fiap.quod.service.DispositivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    @Autowired
    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @PostMapping
    public ResponseEntity<DispositivoDTO> createDispositivo(@Valid @RequestBody DispositivoDTO dispositivoDTO) {
        DispositivoDTO created = dispositivoService.createDispositivo(dispositivoDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispositivoDTO> getDispositivo(@PathVariable Long id) {
        return ResponseEntity.ok(dispositivoService.getDispositivo(id));
    }

    @GetMapping
    public ResponseEntity<List<DispositivoDTO>> getAllDispositivos() {
        return ResponseEntity.ok(dispositivoService.getAllDispositivos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DispositivoDTO> updateDispositivo(@PathVariable Long id,
                                                            @Valid @RequestBody DispositivoDTO dispositivoDTO) {
        DispositivoDTO updated = dispositivoService.updateDispositivo(id, dispositivoDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispositivo(@PathVariable Long id) {
        dispositivoService.deleteDispositivo(id);
        return ResponseEntity.noContent().build();
    }
}
