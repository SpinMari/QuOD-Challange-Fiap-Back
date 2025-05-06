package com.br.fiap.quod.dto;

import com.br.fiap.quod.domain.Dispositivo;

public record DispositivoDTO(String fabricante, String modelo, String sistemaOperacional) {
    public DispositivoDTO(String fabricante, String modelo, String sistemaOperacional) {
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.sistemaOperacional = sistemaOperacional;
    }

    public DispositivoDTO(Dispositivo dispositivo) {
        this(
                dispositivo.getFabricante(),
                dispositivo.getModelo(),
                dispositivo.getSistemaOperacional()
        );
    }
}