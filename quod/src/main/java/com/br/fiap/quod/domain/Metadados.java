package com.br.fiap.quod.domain;

import com.br.fiap.quod.dto.MetadadosDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")


public class Metadados {

    @Id
    @GeneratedValue
    private Long id;
    private double latitude;
    private double longitude;
    private String ipOrigem;
    private String formato;
    private int altura;
    private int largura;
    private int dpi;


    public Metadados(MetadadosDTO dto) {
        this.latitude = dto.latitude();
        this.longitude = dto.longitude();
        this.ipOrigem = dto.ipOrigem();
    }


}
