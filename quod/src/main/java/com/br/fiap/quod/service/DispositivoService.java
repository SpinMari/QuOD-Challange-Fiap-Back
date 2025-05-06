package com.br.fiap.quod.service;

import com.br.fiap.quod.domain.Dispositivo;
import com.br.fiap.quod.dto.DispositivoDTO;
import com.br.fiap.quod.repository.DispositivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;

    @Autowired
    public DispositivoService(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    private void validateDispositivo(DispositivoDTO dispositivo) {
        if (dispositivo == null) {
            throw new IllegalArgumentException("Dispositivo não pode ser nulo");
        }
        // Você pode adicionar validações mais específicas aqui
    }

    public DispositivoDTO createDispositivo(DispositivoDTO dto) {
        validateDispositivo(dto);
        Dispositivo saved = dispositivoRepository.save(new Dispositivo(dto));
        return new DispositivoDTO(saved);
    }

    public DispositivoDTO updateDispositivo(Long id, DispositivoDTO dto) {
        validateDispositivo(dto);

        Dispositivo existing = dispositivoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado com id: " + id));

        // Atualiza campos manualmente
        existing.setFabricante(dto.fabricante());
        existing.setModelo(dto.modelo());
        existing.setSistemaOperacional(dto.sistemaOperacional());

        Dispositivo updated = dispositivoRepository.save(existing);
        return new DispositivoDTO(updated);
    }

    public DispositivoDTO getDispositivo(Long id) {
        return dispositivoRepository.findById(id)
                .map(DispositivoDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado com id: " + id));
    }

    public List<DispositivoDTO> getAllDispositivos() {
        return dispositivoRepository.findAll().stream()
                .map(DispositivoDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteDispositivo(Long id) {
        if (!dispositivoRepository.existsById(id)) {
            throw new IllegalArgumentException("Dispositivo não encontrado com id: " + id);
        }
        dispositivoRepository.deleteById(id);
    }
}
