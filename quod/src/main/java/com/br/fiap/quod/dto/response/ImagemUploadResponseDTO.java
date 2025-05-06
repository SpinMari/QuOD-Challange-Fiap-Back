package com.br.fiap.quod.dto.response;

public record ImagemUploadResponseDTO(
        String imageId,
        String tipoBiometria,
        boolean fraudeDetectada
) {
    public ImagemUploadResponseDTO(String imageId, String tipoBiometria, boolean fraudeDetectada) {
        this.imageId = imageId;
        this.tipoBiometria = tipoBiometria;
        this.fraudeDetectada = fraudeDetectada;
    }
}