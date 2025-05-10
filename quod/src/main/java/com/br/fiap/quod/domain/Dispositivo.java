package com.br.fiap.quod.domain;

import com.br.fiap.quod.dto.DispositivoDTO;

public class Dispositivo {

    private String id;
    private String fabricante;
    private String modelo;
    private String sistemaOperacional;
    private Metadados metadados;

    public Dispositivo() {}

    public Dispositivo(DispositivoDTO dto) {
        this.fabricante = dto.fabricante();
        this.modelo = dto.modelo();
        this.sistemaOperacional = dto.sistemaOperacional();
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getSistemaOperacional() { return sistemaOperacional; }
    public void setSistemaOperacional(String sistemaOperacional) { this.sistemaOperacional = sistemaOperacional; }

    public Metadados getMetadados() { return metadados; }
    public void setMetadados(Metadados metadados) { this.metadados = metadados; }
}
