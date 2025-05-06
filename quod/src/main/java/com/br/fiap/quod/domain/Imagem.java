package com.br.fiap.quod.domain;

import com.br.fiap.quod.dto.request.ImagemUploadRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Imagem {

    @Id
    @GeneratedValue
    private String id;
    private String filename;
    private String tipoBiometria;
    private LocalDateTime dataCaptura;

    //itens comentados, pois precisa vere se serao manytoone ou oque
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dispositivo_id")
    private Dispositivo dispositivo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "metadados_id")
    private Metadados metadados;
    private boolean fraudeDetectada;



}