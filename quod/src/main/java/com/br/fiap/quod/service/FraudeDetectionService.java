package com.br.fiap.quod.service;


import com.br.fiap.quod.domain.Imagem;
import com.br.fiap.quod.domain.Metadados;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class FraudeDetectionService {
    
    public boolean detectarFraude(Imagem imagem) {
        if (imagem == null || imagem.getFilename() == null) {
            return false;
        }

        // 1. Check metadata for suspicious patterns
        Metadados metadados = imagem.getMetadados();
        if (metadados != null) {
            // Check for suspicious locations or IP addresses
            if (isLocationSuspicious(metadados.getLatitude(), metadados.getLongitude()) || 
                isIpSuspicious(metadados.getIpOrigem())) {
                return true;
            }
        }

        // 2. Check biometria type and validate
        String tipoBiometria = imagem.getTipoBiometria();
        if (tipoBiometria != null) {
            if ("FACIAL".equalsIgnoreCase(tipoBiometria)) {
                // For facial biometry, use facial recognition checks
                return detectFacialFraud(imagem);
            } else if ("DOCUMENT".equalsIgnoreCase(tipoBiometria)) {
                // For fingerprint biometry, check fingerprint patterns
                return detectDocumentFraud(imagem);
            }
        }

        // 3. Check capture timing patterns
        LocalDateTime captureTime = imagem.getDataCaptura();
        if (captureTime != null && isTimingSuspicious(captureTime)) {
            return true;
        }


        // 4. If all checks passed, return no fraud detected
        return false;
    }

    private boolean isTimingSuspicious(LocalDateTime captureTime) {
        // Define suspicious time ranges (late night/early morning)
        int suspiciousStartHour = 23; // 11 PM
        int suspiciousEndHour = 5;    // 5 AM

        // Get the hour of capture
        int captureHour = captureTime.getHour();

        // Check if capture time falls within suspicious hours
        if (captureHour >= suspiciousStartHour || captureHour <= suspiciousEndHour) {
            return true;
        }

        // Check for rapid succession captures (if repository data was available)
        // This would require checking previous captures from the same source

        // Check for unusual patterns in minutes/seconds
        int captureMinute = captureTime.getMinute();
        int captureSecond = captureTime.getSecond();

        // Suspicious if exact hours with 0 minutes and seconds (potentially automated)
        if (captureMinute == 0 && captureSecond == 0) {
            return true;
        }

        return false;
    }

    private boolean detectDocumentFraud(Imagem imagem) {
        if (imagem == null || imagem.getMetadados() == null) {
            return true;
        }

        // Check document format and structure
        if (!isValidDocumentFormat(imagem)) {
            return true;
        }

        // Check for visual manipulation patterns
        if (hasVisualManipulationPatterns(imagem)) {
            return true;
        }

        // Verify metadata consistency
        Metadados metadados = imagem.getMetadados();
        if (!isMetadataConsistent(metadados)) {
            return true;
        }

        // Check OCR content validity
        String documentText = extractDocumentText(imagem);
        if (!isValidDocumentContent(documentText)) {
            return true;
        }

        return false;
    }

    private boolean isValidDocumentFormat(Imagem imagem) {
        // Define valid document parameters
        final int MIN_WIDTH = 1000;
        final int MIN_HEIGHT = 600;
        final double EXPECTED_ASPECT_RATIO = 1.6; // Common document ratio
        final double ASPECT_RATIO_TOLERANCE = 0.1;
        final Set<String> VALID_FORMATS = Set.of("jpg", "jpeg", "png", "pdf");

        if (imagem == null || imagem.getMetadados() == null) {
            return false;
        }

        // Check file format
        String fileFormat = String.valueOf(imagem.getMetadados().getFormato());
        if (fileFormat == null || !VALID_FORMATS.contains(fileFormat.toLowerCase())) {
            return false;
        }

        // Check dimensions
        int width = imagem.getMetadados().getLargura();
        int height = imagem.getMetadados().getAltura();

        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            return false;
        }

        // Check resolution (DPI)
        int dpi = imagem.getMetadados().getDpi();
        if (dpi < 200) { // Minimum 200 DPI for document quality
            return false;
        }

        // Validate aspect ratio
        double aspectRatio = (double) width / height;
        double aspectRatioDifference = Math.abs(aspectRatio - EXPECTED_ASPECT_RATIO);

        if (aspectRatioDifference > ASPECT_RATIO_TOLERANCE) {
            return false;
        }

        return true;
    }

    private boolean hasVisualManipulationPatterns(Imagem imagem) {
        if (imagem == null || imagem.getMetadados() == null) {
            return true;
        }

        // Check for compression artifacts
        double compressionLevel = calculateCompressionArtifacts(imagem);
        if (compressionLevel > 0.7) { // High compression indicates potential manipulation
            return true;
        }

        // Check for edge inconsistencies
        if (hasEdgeInconsistencies(imagem)) {
            return true;
        }

        // Check for color anomalies
        if (hasColorAnomalies(imagem)) {
            return true;
        }

        // Check file size vs. dimensions ratio
        long fileSize = Long.parseLong(imagem.getMetadados().getFormato()); // Assuming formato holds file size
        int width = imagem.getMetadados().getLargura();
        int height = imagem.getMetadados().getAltura();
        double expectedSizeRatio = (width * height) / 1024.0; // Expected size in KB

        if (fileSize < expectedSizeRatio * 0.1) { // Suspicious if file is too small for dimensions
            return true;
        }

        return false;
    }

    private double calculateCompressionArtifacts(Imagem imagem) {
        if (imagem == null || imagem.getMetadados() == null) {
            return 1.0; // High compression score indicates potential fraud
        }

        // Calculate compression ratio based on file size and dimensions
        long fileSize = Long.parseLong(imagem.getMetadados().getFormato());
        int width = imagem.getMetadados().getLargura();
        int height = imagem.getMetadados().getAltura();

        double theoreticalSize = width * height * 3; // 3 bytes per pixel (RGB)
        double compressionRatio = fileSize / theoreticalSize;

        // Normalize compression ratio to 0-1 scale
        double normalizedRatio = Math.min(1.0, compressionRatio);

        // Apply weighting factors
        double qualityScore = 1.0 - normalizedRatio;

        // Factor in resolution
        int dpi = imagem.getMetadados().getDpi();
        if (dpi < 72) {
            qualityScore *= 0.5; // Penalize low resolution
        }

        // Return inverse of quality score (higher compression = higher score)
        return 1.0 - qualityScore;
    }

    private boolean hasEdgeInconsistencies(Imagem imagem) {

        if (imagem == null || imagem.getMetadados() == null) {
            return true;
        }

        // Constants for edge detection
        final double EDGE_THRESHOLD = 0.8;
        final int MIN_EDGE_COUNT = 100;
        final double CONSISTENCY_THRESHOLD = 0.7;

        // Get image dimensions
        int width = imagem.getMetadados().getLargura();
        int height = imagem.getMetadados().getAltura();

        // Check minimum dimensions
        if (width < 100 || height < 100) {
            return true;
        }

        // Check edge density
        double edgeDensity = calculateEdgeDensity(width, height);
        if (edgeDensity > EDGE_THRESHOLD) {
            return true;
        }

        // Check edge count
        int edgeCount = countSignificantEdges(width, height);
        if (edgeCount < MIN_EDGE_COUNT) {
            return true;
        }

        // Check edge consistency across regions
        double consistencyScore = calculateEdgeConsistency(width, height);
        if (consistencyScore < CONSISTENCY_THRESHOLD) {
            return true;
        }

        return false;
    }

    private double calculateEdgeDensity(int width, int height) {

        // Constants for edge detection
        final double STRONG_EDGE_THRESHOLD = 100.0;
        final int WINDOW_SIZE = 3;

        // Sobel operators for edge detection
        final int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        final int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        // Calculate number of strong edges
        int strongEdges = 0;
        int totalPixels = width * height;

        // Simulate edge detection using dimensions
        for (int y = WINDOW_SIZE / 2; y < height - WINDOW_SIZE / 2; y++) {
            for (int x = WINDOW_SIZE / 2; x < width - WINDOW_SIZE / 2; x++) {
                // Calculate gradients using dimensions as proxy
                double gradientX = Math.abs((x + 1) - (x - 1));
                double gradientY = Math.abs((y + 1) - (y - 1));

                // Calculate edge magnitude
                double magnitude = Math.sqrt(gradientX * gradientX + gradientY * gradientY);

                if (magnitude > STRONG_EDGE_THRESHOLD) {
                    strongEdges++;
                }
            }
        }

        // Calculate edge density as ratio of strong edges to total pixels
        return (double) strongEdges / totalPixels;
    }

    private int countSignificantEdges(int width, int height) {
        // Constants for edge detection
        final int GRID_SIZE = 16;
        final double EDGE_THRESHOLD = 50.0;

        int significantEdges = 0;

        // Process image in grid cells
        for (int y = 0; y < height - GRID_SIZE; y += GRID_SIZE) {
            for (int x = 0; x < width - GRID_SIZE; x += GRID_SIZE) {
                // Simulate gradient calculation for current grid cell
                double horizontalGradient = Math.abs((x + GRID_SIZE) - x);
                double verticalGradient = Math.abs((y + GRID_SIZE) - y);

                // Calculate magnitude of edge response
                double edgeMagnitude = Math.sqrt(
                        horizontalGradient * horizontalGradient +
                                verticalGradient * verticalGradient
                );

                // Count significant edges based on threshold
                if (edgeMagnitude > EDGE_THRESHOLD) {
                    significantEdges++;
                }
            }
        }

        return significantEdges;
    }

    private double calculateEdgeConsistency(int width, int height) {

        // Constants for region analysis
        final int REGION_SIZE = 32;
        final double MIN_EDGE_VARIANCE = 0.3;
        final double MAX_EDGE_VARIANCE = 0.9;

        // Validate input parameters
        if (width <= 0 || height <= 0) {
            return 0.0;
        }

        // Calculate number of regions
        int regionsX = width / REGION_SIZE;
        int regionsY = height / REGION_SIZE;

        if (regionsX == 0 || regionsY == 0) {
            return 0.0;
        }

        // Calculate edge density for each region
        double totalVariance = 0.0;
        double[][] regionDensities = new double[regionsX][regionsY];

        for (int x = 0; x < regionsX; x++) {
            for (int y = 0; y < regionsY; y++) {
                regionDensities[x][y] = calculateEdgeDensity(
                        x * REGION_SIZE,
                        y * REGION_SIZE
                );
            }
        }

        // Calculate variance between adjacent regions
        for (int x = 0; x < regionsX - 1; x++) {
            for (int y = 0; y < regionsY - 1; y++) {
                double variance = Math.abs(regionDensities[x][y] - regionDensities[x + 1][y])
                        + Math.abs(regionDensities[x][y] - regionDensities[x][y + 1]);
                totalVariance += variance;
            }
        }

        // Normalize variance and calculate consistency score
        double averageVariance = totalVariance / ((regionsX - 1) * (regionsY - 1));
        if (averageVariance < MIN_EDGE_VARIANCE) {
            return 1.0; // High consistency
        } else if (averageVariance > MAX_EDGE_VARIANCE) {
            return 0.0; // Low consistency
        }

        // Return normalized consistency score
        return 1.0 - (averageVariance - MIN_EDGE_VARIANCE) / (MAX_EDGE_VARIANCE - MIN_EDGE_VARIANCE);
    }

    private boolean hasColorAnomalies(Imagem imagem) {
        // Constants for color analysis
        final double COLOR_VARIANCE_THRESHOLD = 0.3;
        final double PATTERN_THRESHOLD = 0.8;
        final int MIN_COLOR_COUNT = 1000;
        final double CONSISTENCY_THRESHOLD = 0.6;

        if (imagem == null || imagem.getMetadados() == null) {
            return true;
        }

        // Get image dimensions
        int width = imagem.getMetadados().getLargura();
        int height = imagem.getMetadados().getAltura();

        // Check minimum dimensions
        if (width < 100 || height < 100) {
            return true;
        }

        // Calculate color distribution score
        double colorVariance = width * height / MIN_COLOR_COUNT;
        if (colorVariance < COLOR_VARIANCE_THRESHOLD) {
            return true;
        }

        // Check for unnatural color patterns
        double patternScore = calculatePatternScore(width, height);
        if (patternScore > PATTERN_THRESHOLD) {
            return true;
        }

        // Analyze color consistency across regions
        double consistencyScore = calculateColorConsistency(width, height);
        if (consistencyScore < CONSISTENCY_THRESHOLD) {
            return true;
        }

        return false;
    }

    private int calculateColorConsistency(int width, int height) {
        // Constants for region analysis
        final int REGION_SIZE = 32;
        final double MIN_VARIANCE = 0.2;
        final double MAX_VARIANCE = 0.8;

        if (width <= 0 || height <= 0) {
            return 0;
        }

        // Calculate number of regions
        int regionsX = width / REGION_SIZE;
        int regionsY = height / REGION_SIZE;

        if (regionsX == 0 || regionsY == 0) {
            return 0;
        }

        // Simulate color analysis for each region
        double totalVariance = 0.0;
        for (int x = 0; x < regionsX; x++) {
            for (int y = 0; y < regionsY; y++) {
                // Calculate simulated color variance for region
                double regionVariance = (double) (x + y) / (regionsX + regionsY);
                totalVariance += regionVariance;
            }
        }

        // Normalize variance
        double averageVariance = totalVariance / (regionsX * regionsY);
        if (averageVariance < MIN_VARIANCE || averageVariance > MAX_VARIANCE) {
            return 0;
        }

        return (int) ((averageVariance - MIN_VARIANCE) / (MAX_VARIANCE - MIN_VARIANCE) * 100);
    }

    private int calculatePatternScore(int width, int height) {
        // Constants for pattern detection
        final int BLOCK_SIZE = 16;
        final double PATTERN_THRESHOLD = 0.7;

        if (width <= 0 || height <= 0) {
            return 0;
        }

        // Calculate blocks
        int blocksX = width / BLOCK_SIZE;
        int blocksY = height / BLOCK_SIZE;

        if (blocksX == 0 || blocksY == 0) {
            return 0;
        }

        // Simulate pattern detection across blocks
        int patternCount = 0;
        for (int x = 0; x < blocksX - 1; x++) {
            for (int y = 0; y < blocksY - 1; y++) {
                // Simulate pattern matching between adjacent blocks
                double similarity = Math.random(); // Simplified simulation
                if (similarity > PATTERN_THRESHOLD) {
                    patternCount++;
                }
            }
        }

        // Calculate pattern score as percentage
        double totalBlocks = (blocksX - 1) * (blocksY - 1);
        return (int) ((patternCount / totalBlocks) * 100);
    }

    private boolean isMetadataConsistent(Metadados metadados) {

        if (metadados == null) {
            return false;
        }

        // Check basic required fields
        if (metadados.getFormato() == null ||
                metadados.getLargura() <= 0 ||
                metadados.getAltura() <= 0) {
            return false;
        }

        // Validate image dimensions
        if (metadados.getLargura() > 10000 || metadados.getAltura() > 10000) {
            return false;
        }

        // Validate DPI
        if (metadados.getDpi() < 72 || metadados.getDpi() > 1200) {
            return false;
        }

        // Check file format
        String formato = String.valueOf(metadados.getFormato()).toLowerCase();
        Set<String> validFormats = Set.of("jpg", "jpeg", "png", "pdf");
        if (!validFormats.contains(formato)) {
            return false;
        }

        // Additional size validation
        long fileSize = Long.parseLong(metadados.getFormato()); // Assuming formato contains file size
        long expectedMaxSize = (long) metadados.getLargura() * metadados.getAltura() * 3; // 3 bytes per pixel
        if (fileSize > expectedMaxSize) {
            return false;
        }

        return true;
    }

    private String extractDocumentText(Imagem imagem) {
        if (imagem == null || imagem.getMetadados() == null) {
            return "";
        }

        try {
            // Initialize OCR parameters
            double confidenceThreshold = 80.0;
            String language = "por";  // Portuguese language

            // Extract text based on image dimensions and format
            StringBuilder extractedText = new StringBuilder();
            int width = imagem.getMetadados().getLargura();
            int height = imagem.getMetadados().getAltura();

            // Simulate OCR processing based on image properties
            if (width >= 800 && height >= 600) {
                // Basic document structure extraction
                extractedText.append("DOCUMENTO ");
                extractedText.append(String.format("%dx%d ", width, height));

                // Add format information
                String formato = imagem.getMetadados().getFormato();
                if (formato != null) {
                    extractedText.append("FORMATO: ").append(formato.toUpperCase());
                }

                // Add DPI information
                int dpi = imagem.getMetadados().getDpi();
                if (dpi > 0) {
                    extractedText.append(" DPI: ").append(dpi);
                }
            }

            return extractedText.toString().trim();

        } catch (Exception e) {
            // Log error and return empty string on failure
            return "";
        }
    }

    private boolean isValidDocumentContent(String documentText) {
        // Constants for validation
        final int MIN_TEXT_LENGTH = 10;
        final int MAX_TEXT_LENGTH = 10000;
        final Set<String> REQUIRED_KEYWORDS = Set.of("DOCUMENTO", "FORMATO", "DPI");

        // Check for null or empty text
        if (documentText == null || documentText.trim().isEmpty()) {
            return false;
        }

        // Validate text length
        int textLength = documentText.length();
        if (textLength < MIN_TEXT_LENGTH || textLength > MAX_TEXT_LENGTH) {
            return false;
        }

        // Check for required keywords
        boolean hasAllKeywords = REQUIRED_KEYWORDS.stream()
                .allMatch(keyword -> documentText.toUpperCase().contains(keyword));
        if (!hasAllKeywords) {
            return false;
        }

        // Validate document format pattern (e.g., "DOCUMENTO 1234x5678 FORMATO: JPG DPI: 300")
        String pattern = "DOCUMENTO \\d+x\\d+ FORMATO: [A-Z]+ DPI: \\d+";
        if (!documentText.toUpperCase().matches(pattern)) {
            return false;
        }

        return true;
    }

    private boolean detectFacialFraud(Imagem imagem) {
        if (imagem == null || imagem.getMetadados() == null) {
            return true;
        }

        // Constants for facial detection
        final int MIN_FACE_WIDTH = 150;
        final int MIN_FACE_HEIGHT = 150;
        final double FACE_ASPECT_RATIO = 1.0;
        final double ASPECT_RATIO_TOLERANCE = 0.2;
        final int MIN_DPI = 200;

        Metadados metadados = imagem.getMetadados();

        // Check image dimensions
        int width = metadados.getLargura();
        int height = metadados.getAltura();

        if (width < MIN_FACE_WIDTH || height < MIN_FACE_HEIGHT) {
            return true;
        }

        // Check image resolution
        if (metadados.getDpi() < MIN_DPI) {
            return true;
        }

        // Check aspect ratio for face
        double aspectRatio = (double) width / height;
        if (Math.abs(aspectRatio - FACE_ASPECT_RATIO) > ASPECT_RATIO_TOLERANCE) {
            return true;
        }

        // Validate face features
        if (!hasValidFaceFeatures(width, height)) {
            return true;
        }

        // Check for liveness indicators
        if (!hasLivenessIndicators(metadados)) {
            return true;
        }

        return false;
    }

    private boolean hasValidFaceFeatures(int width, int height) {
        // Minimum thresholds for facial features
        final double MIN_EYE_DISTANCE = width * 0.2;
        final double MIN_NOSE_LENGTH = height * 0.2;
        final double MIN_MOUTH_WIDTH = width * 0.3;

        // Simulate feature detection using image dimensions
        double simulatedEyeDistance = width * 0.25; // Expected ~25% of face width
        double simulatedNoseLength = height * 0.25; // Expected ~25% of face height
        double simulatedMouthWidth = width * 0.35; // Expected ~35% of face width

        // Validate features meet minimum requirements
        return simulatedEyeDistance >= MIN_EYE_DISTANCE &&
                simulatedNoseLength >= MIN_NOSE_LENGTH &&
                simulatedMouthWidth >= MIN_MOUTH_WIDTH;
    }

    private boolean hasLivenessIndicators(Metadados metadados) {
        // Check basic liveness indicators based on metadata
        if (metadados == null) {
            return false;
        }

        // Validate image quality indicators
        int dpi = metadados.getDpi();
        int width = metadados.getLargura();
        int height = metadados.getAltura();

        // Minimum thresholds for liveness detection
        final int MIN_LIVENESS_DPI = 300;
        final int MIN_DIMENSION = 480;
        final String REQUIRED_FORMAT = "jpg";

        // Check resolution and dimensions
        if (dpi < MIN_LIVENESS_DPI || width < MIN_DIMENSION || height < MIN_DIMENSION) {
            return false;
        }

        // Check format requirements
        String formato = metadados.getFormato();
        if (formato == null || !formato.toLowerCase().contains(REQUIRED_FORMAT)) {
            return false;
        }

        return true;
    }

    private boolean isIpSuspicious(String ipOrigem) {
        if (ipOrigem == null || ipOrigem.trim().isEmpty()) {
            return true;
        }

        // IP format validation pattern
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        if (!ipOrigem.matches(ipPattern)) {
            return true;
        }

        // Known suspicious IP ranges
        String[] suspiciousRanges = {
                "0.0.0.0/8",      // Reserved for self-identification
                "10.0.0.0/8",     // Private network
                "100.64.0.0/10",  // Carrier-grade NAT
                "127.0.0.0/8",    // Localhost
                "169.254.0.0/16", // Link-local
                "172.16.0.0/12",  // Private network
                "192.0.0.0/24",   // IETF Protocol Assignments
                "192.0.2.0/24",   // TEST-NET-1
                "192.168.0.0/16", // Private network
                "198.18.0.0/15",  // Network benchmark testing
                "198.51.100.0/24",// TEST-NET-2
                "203.0.113.0/24", // TEST-NET-3
                "224.0.0.0/4",    // Multicast
                "240.0.0.0/4"     // Reserved for future use
        };

        // Convert IP string to long for range comparison
        String[] octets = ipOrigem.split("\\.");
        long ip = 0;
        for (String octet : octets) {
            ip = ip << 8 | Integer.parseInt(octet);
        }

        // Check if IP falls within suspicious ranges
        for (String range : suspiciousRanges) {
            String[] parts = range.split("/");
            String[] baseOctets = parts[0].split("\\.");
            int prefixLength = Integer.parseInt(parts[1]);

            long baseIp = 0;
            for (String octet : baseOctets) {
                baseIp = baseIp << 8 | Integer.parseInt(octet);
            }

            long mask = -1L << (32 - prefixLength);
            if ((ip & mask) == (baseIp & mask)) {
                return true;
            }
        }

        return false;
    }

    private boolean isLocationSuspicious(double latitude, double longitude) {
        // Constants for location validation
        final double MIN_LATITUDE = -90.0;
        final double MAX_LATITUDE = 90.0;
        final double MIN_LONGITUDE = -180.0;
        final double MAX_LONGITUDE = 180.0;

        // Validate coordinate ranges
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE ||
                longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            return true;
        }

        // Known suspicious locations (coordinates of data centers, VPN servers, etc.)
        double[][] suspiciousLocations = {
                {0.0, 0.0},      // Null Island
                {51.5074, -0.1278}, // London (high fraud rate location)
                {40.7128, -74.0060}, // New York (high fraud rate location)
                {-23.5505, -46.6333}, // São Paulo (high fraud rate location)
                {39.9042, 116.4074}  // Beijing (high fraud rate location)
        };

        // Check proximity to suspicious locations
        double proximityThreshold = 0.1; // Approximately 11km
        for (double[] location : suspiciousLocations) {
            double distance = calculateDistance(latitude, longitude, location[0], location[1]);
            if (distance < proximityThreshold) {
                return true;
            }
        }

        // Check for precise/round number coordinates (potentially fake)
        if (isRoundNumber(latitude) && isRoundNumber(longitude)) {
            return true;
        }

        return false;
    }

    private boolean isRoundNumber(double value) {
        // Check if number has no decimal places or very regular pattern
        final double PRECISION = 0.0001;
        final double[] COMMON_GEO_VALUES = {0.0, 30.0, 45.0, 60.0, 90.0, 180.0};
        final double COMMON_FRACTION = 0.5;

        // Check if value is too close to a whole number
        if (Math.abs(value - Math.round(value)) < PRECISION) {
            return true;
        }

        // Check for common geographical values
        for (double geoValue : COMMON_GEO_VALUES) {
            if (Math.abs(Math.abs(value) - geoValue) < PRECISION) {
                return true;
            }
        }

        // Check for common fractions (e.g., X.5)
        double fraction = Math.abs(value - Math.floor(value));
        if (Math.abs(fraction - COMMON_FRACTION) < PRECISION) {
            return true;
        }

        // Check for repeating patterns in decimals
        String decimalStr = String.format("%.6f", Math.abs(value));
        String decimals = decimalStr.substring(decimalStr.indexOf('.') + 1);

        // Check for repeating digits (e.g., 12.3333)
        if (decimals.matches("(\\d)\\1{3,}")) {
            return true;
        }

        // Check for sequential patterns (e.g., 12.3456)
        if (decimals.matches("0*12345|23456|34567|45678|56789|" +
                "98765|87654|76543|65432|54321")) {
            return true;
        }

        return false;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate distance between coordinates
        final int EARTH_RADIUS = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}