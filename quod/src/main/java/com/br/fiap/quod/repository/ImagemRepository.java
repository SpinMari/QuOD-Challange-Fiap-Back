package com.br.fiap.quod.repository;

import com.br.fiap.quod.domain.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ImagemRepository<Pageable> extends JpaRepository<Imagem, Long> {
    // Find images by fraud detection status
    List<Imagem> findByFraudeDetectada(boolean fraudeDetectada);

    // Find images by biometric type
    List<Imagem> findByTipoBiometria(String tipoBiometria);

    // Find images by biometric type and fraud detection status
    List<Imagem> findByTipoBiometriaAndFraudeDetectada(String tipoBiometria, boolean fraudeDetectada);

    // Find images captured within a date range
    List<Imagem> findByDataCapturaBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find images by device ID (assuming Dispositivo has an ID field)
    List<Imagem> findByDispositivo_Id(String dispositivoId);

    // Count images by biometric type
    long countByTipoBiometria(String tipoBiometria);

    // Delete images older than a certain date
    @Modifying
    @Transactional
    @Query("DELETE FROM Imagem i WHERE i.dataCaptura < :date")
    void deleteImagesOlderThan(@Param("date") LocalDateTime date);

    // Find the most recent images, ordered by capture date
    @Query("SELECT i FROM Imagem i ORDER BY i.dataCaptura DESC")
    List<Imagem> findRecentImages(Pageable pageable);

    // Search images by metadata values (assuming Metadados has a field like 'tags')
    List<Imagem> findByMetadados_TagsContaining(String tag);
}