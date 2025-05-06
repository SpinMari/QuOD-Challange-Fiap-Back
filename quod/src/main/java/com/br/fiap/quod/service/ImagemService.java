package com.br.fiap.quod.service;

import com.br.fiap.quod.domain.Imagem;
import com.br.fiap.quod.domain.Dispositivo;
import com.br.fiap.quod.domain.Metadados;
import com.br.fiap.quod.dto.request.ImagemUploadRequestDTO;
import com.br.fiap.quod.dto.response.ImagemUploadResponseDTO;
import com.br.fiap.quod.repository.ImagemRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImagemService {

    private final ImagemRepository imagemRepository;

    public ImagemService(ImagemRepository imagemRepository) {
        this.imagemRepository = imagemRepository;
    }

    public ImagemUploadResponseDTO uploadImagem(ImagemUploadRequestDTO request) {
        String processedImagePath = processImage(request.imagem());
        boolean fraudeDetectada = detectFraude(processedImagePath, request.tipoBiometria());

        Imagem imagem = createImagem(request, processedImagePath, fraudeDetectada);

        imagemRepository.save(imagem);

        return new ImagemUploadResponseDTO(imagem.getId(), imagem.getTipoBiometria(), fraudeDetectada);
    }

    private String processImage(String imagemBase64) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(imagemBase64);
            String filename = "imagem_" + System.currentTimeMillis() + ".jpg";
            String filePath = "uploads/" + filename;

            File directory = new File("uploads");
            if (!directory.exists()) directory.mkdirs();

            Files.write(Paths.get(filePath), imageBytes);

            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar imagem", e);
        }
    }

    private boolean detectFraude(String imagePath, String tipoBiometria) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return switch (tipoBiometria.toUpperCase()) {
                case "FACIAL" -> runFacialFraudDetection(image);
                case "DIGITAL" -> runFingerprintFraudDetection(image);
                default -> false;
            };
        } catch (Exception e) {
            System.err.println("Erro na detecção de fraude: " + e.getMessage());
            return true;
        }
    }

    private boolean runFacialFraudDetection(BufferedImage image) {
        return false;
    }

    private boolean runFingerprintFraudDetection(BufferedImage image) {
        return false;
    }

    private Imagem createImagem(ImagemUploadRequestDTO request, String imagePath, boolean fraudeDetectada) {
        Imagem imagem = new Imagem();
        imagem.setFilename(imagePath);
        imagem.setTipoBiometria(request.tipoBiometria());
        imagem.setFraudeDetectada(fraudeDetectada);
        imagem.setDataCaptura(request.dataCaptura() != null
                ? LocalDateTime.parse(request.dataCaptura())
                : LocalDateTime.now());

        if (request.dispositivo() != null)
            imagem.setDispositivo(new Dispositivo(request.dispositivo()));

        if (request.metadados() != null)
            imagem.setMetadados(new Metadados(request.metadados()));

        return imagem;
    }
}
