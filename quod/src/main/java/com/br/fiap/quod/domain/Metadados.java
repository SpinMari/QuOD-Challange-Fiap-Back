package com.br.fiap.quod.domain;

import com.br.fiap.quod.dto.MetadadosDTO;

public class Metadados {

    private double latitude;
    private double longitude;
    private String ipOrigem;
    private String formato;
    private int altura;
    private int largura;
    private int dpi;

    public Metadados() {}

    public Metadados(MetadadosDTO dto) {
        this.latitude = dto.latitude();
        this.longitude = dto.longitude();
        this.ipOrigem = dto.ipOrigem();
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getIpOrigem() { return ipOrigem; }
    public void setIpOrigem(String ipOrigem) { this.ipOrigem = ipOrigem; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }

    public int getLargura() { return largura; }
    public void setLargura(int largura) { this.largura = largura; }

    public int getDpi() { return dpi; }
    public void setDpi(int dpi) { this.dpi = dpi; }
}
