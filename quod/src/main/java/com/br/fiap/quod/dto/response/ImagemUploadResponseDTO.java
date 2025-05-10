package com.br.fiap.quod.dto.response;

public record ImagemUploadResponseDTO(
        String imageId,
        String tipoBiometria,
        boolean fraudeDetectada
) {

}