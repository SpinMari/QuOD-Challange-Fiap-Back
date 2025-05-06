package com.br.fiap.quod.domain;

import com.br.fiap.quod.dto.DispositivoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dispositivo {

    @Id
    @GeneratedValue
    private Long id;
    private String fabricante;
    private String modelo;
    private String sistemaOperacional;

    //itens comentados, pois precisa vere se serao manytoone ou oque
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "metadados_id")
    private Metadados metadados;

    public Dispositivo(DispositivoDTO dto) {
        this.fabricante = dto.fabricante();
        this.modelo = dto.modelo();
        this.sistemaOperacional = dto.sistemaOperacional();
    }


}
