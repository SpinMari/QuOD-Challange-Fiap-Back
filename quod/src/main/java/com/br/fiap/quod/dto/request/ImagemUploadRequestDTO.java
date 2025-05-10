package com.br.fiap.quod.dto.request;

import com.br.fiap.quod.domain.Imagem;
import com.br.fiap.quod.dto.DispositivoDTO;
import com.br.fiap.quod.dto.MetadadosDTO;
import jakarta.validation.constraints.NotBlank;

public record ImagemUploadRequestDTO(
        @NotBlank(message = "Imagem é obrigatória")
        String imagem,

        @NotBlank(message = "Tipo de biometria é obrigatório")
        String tipoBiometria,

        String dataCaptura,
        DispositivoDTO dispositivo,
        MetadadosDTO metadados
) {
    public ImagemUploadRequestDTO(String imagem, String tipoBiometria, String dataCaptura, DispositivoDTO dispositivo, MetadadosDTO metadados) {
        this.imagem = imagem;
        this.tipoBiometria = tipoBiometria;
        this.dataCaptura = dataCaptura;
        this.dispositivo = dispositivo;
        this.metadados = metadados;
    }


}
