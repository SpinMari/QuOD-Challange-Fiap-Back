package com.br.fiap.quod.service;

import com.br.fiap.quod.domain.Imagem;
import com.br.fiap.quod.repository.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class MetadadosService {
    private final ImagemRepository imagemRepository;

    @Autowired
    public MetadadosService(ImagemRepository imagemRepository) {
        this.imagemRepository = imagemRepository;
    }

    public List<Imagem> buscarImagensPorTag(String tag) {
        return imagemRepository.findByMetadados_TagsContaining(tag);
    }

    public List<Imagem> buscarImagensPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return imagemRepository.findByDataCapturaBetween(inicio, fim);
    }

    public long contarImagensPorTipoBiometria(String tipoBiometria) {
        return imagemRepository.countByTipoBiometria(tipoBiometria);
    }

    public List<Imagem> buscarImagensPorTipoBiometriaEFraude(String tipoBiometria, boolean fraudeDetectada) {
        return imagemRepository.findByTipoBiometriaAndFraudeDetectada(tipoBiometria, fraudeDetectada);
    }

    public void limparImagensAntigas(LocalDateTime data) {
        imagemRepository.deleteImagesOlderThan(data);
    }
}
