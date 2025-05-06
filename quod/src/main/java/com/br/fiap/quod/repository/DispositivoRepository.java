package com.br.fiap.quod.repository;

import com.br.fiap.quod.domain.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    List<Dispositivo> findByModelo(String modelo);

    List<Dispositivo> findByFabricante(String fabricante);

    List<Dispositivo> findByStatus(String status);

    List<Dispositivo> findByFabricanteAndModelo(String fabricante, String modelo);
}
