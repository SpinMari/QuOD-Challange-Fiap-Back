package com.br.fiap.quod.repository;

import com.br.fiap.quod.domain.Imagem;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ImagemRepository extends MongoRepository<Imagem, String> {

    // Buscar por fraude detectada
    List<Imagem> findByFraudeDetectada(boolean fraudeDetectada);

    // Buscar por tipo de biometria
    List<Imagem> findByTipoBiometria(String tipoBiometria);

    // Buscar por tipo de biometria + fraude
    List<Imagem> findByTipoBiometriaAndFraudeDetectada(String tipoBiometria, boolean fraudeDetectada);

    // Buscar por data de captura entre datas
    List<Imagem> findByDataCapturaBetween(LocalDateTime startDate, LocalDateTime endDate);


    // Buscar por ID de dispositivo (assumindo que o ID é salvo como string embutida)
    List<Imagem> findByDispositivo_Id(String dispositivoId);

    // Contar por tipo de biometria
    long countByTipoBiometria(String tipoBiometria);


    // Buscar imagens mais recentes (paginação)
    List<Imagem> findAllByOrderByDataCapturaDesc(Pageable pageable);

    List<Imagem> findByDataCapturaBefore(LocalDateTime data);

}