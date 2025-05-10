package com.br.fiap.quod.service;

import com.br.fiap.quod.domain.Dispositivo;
import com.br.fiap.quod.dto.DispositivoDTO;
import com.br.fiap.quod.repository.ImagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispositivoService {

    private final ImagemRepository imagemRepository;

    public DispositivoService(ImagemRepository imagemRepository) {
        this.imagemRepository = imagemRepository;
    }

    public DispositivoDTO getByModelo(String modelo) {
        return imagemRepository.findAll().stream()
                .map(Imagem -> Imagem.getDispositivo())
                .filter(d -> d != null && d.getModelo().equalsIgnoreCase(modelo))
                .findFirst()
                .map(DispositivoDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado com modelo: " + modelo));
    }

    public List<DispositivoDTO> getAll() {
        return imagemRepository.findAll().stream()
                .map(Imagem -> Imagem.getDispositivo())
                .filter(d -> d != null)
                .map(DispositivoDTO::new)
                .collect(Collectors.toList());
    }

    public List<DispositivoDTO> getByFabricante(String fabricante) {
        return imagemRepository.findAll().stream()
                .map(Imagem -> Imagem.getDispositivo())
                .filter(d -> d != null && d.getFabricante().equalsIgnoreCase(fabricante))
                .map(DispositivoDTO::new)
                .collect(Collectors.toList());
    }

    public List<DispositivoDTO> getByFabricanteAndModelo(String fabricante, String modelo) {
        return imagemRepository.findAll().stream()
                .map(Imagem -> Imagem.getDispositivo())
                .filter(d -> d != null &&
                        d.getFabricante().equalsIgnoreCase(fabricante) &&
                        d.getModelo().equalsIgnoreCase(modelo))
                .map(DispositivoDTO::new)
                .collect(Collectors.toList());
    }
}