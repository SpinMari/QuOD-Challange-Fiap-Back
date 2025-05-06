package com.br.fiap.quod.dto;

import com.br.fiap.quod.domain.Metadados;

public record MetadadosDTO(double latitude, double longitude, String ipOrigem) {
    public MetadadosDTO(double latitude, double longitude, String ipOrigem) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ipOrigem = ipOrigem;
    }

    public MetadadosDTO(Metadados metadados) {
        this(
                metadados.getLatitude(),
                metadados.getLongitude(),
                metadados.getIpOrigem()
        );
    }
}
