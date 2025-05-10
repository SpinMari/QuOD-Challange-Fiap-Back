package com.br.fiap.quod.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "imagens")
public class Imagem {

    @Id
    private String id;
    private String filename;
    private String tipoBiometria;
    private LocalDateTime dataCaptura;
    private boolean fraudeDetectada;

    private Dispositivo dispositivo;
    private Metadados metadados;

    public Imagem() {}

    public Imagem(String filename, String tipoBiometria, LocalDateTime dataCaptura, boolean fraudeDetectada,
                  Dispositivo dispositivo, Metadados metadados) {
        this.filename = filename;
        this.tipoBiometria = tipoBiometria;
        this.dataCaptura = dataCaptura;
        this.fraudeDetectada = fraudeDetectada;
        this.dispositivo = dispositivo;
        this.metadados = metadados;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getTipoBiometria() { return tipoBiometria; }
    public void setTipoBiometria(String tipoBiometria) { this.tipoBiometria = tipoBiometria; }

    public LocalDateTime getDataCaptura() { return dataCaptura; }
    public void setDataCaptura(LocalDateTime dataCaptura) { this.dataCaptura = dataCaptura; }

    public boolean isFraudeDetectada() { return fraudeDetectada; }
    public void setFraudeDetectada(boolean fraudeDetectada) { this.fraudeDetectada = fraudeDetectada; }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }

    public Metadados getMetadados() { return metadados; }
    public void setMetadados(Metadados metadados) { this.metadados = metadados; }
}
