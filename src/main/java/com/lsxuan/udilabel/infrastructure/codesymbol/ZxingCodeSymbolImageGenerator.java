package com.lsxuan.udilabel.infrastructure.codesymbol;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lsxuan.udilabel.domain.label.CodeType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component("zxingCodeSymbolImageGenerator")
public class ZxingCodeSymbolImageGenerator implements CodeSymbolImageGenerator {

    private static final int BARCODE_WIDTH = 500;
    private static final int BARCODE_HEIGHT = 150;

    private static final int QR_CODE_SIZE = 300;
    private static final int DATA_MATRIX_SIZE = 300;

    private static final int MARGIN = 2;

    @Override
    public byte[] generate(CodeType codeType, String codeContent) {
        Objects.requireNonNull(codeType, "codeType cannot be null.");

        if (codeContent == null || codeContent.isBlank()) {
            throw new IllegalArgumentException("codeContent cannot be empty.");
        }

        BarcodeFormat format = resolveFormat(codeType);

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, MARGIN);

        if (codeType == CodeType.QR_CODE) {
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M);
        }

        // GS1 types need GS1-specific data and encoding rules.
        // Do not assume that choosing a BarcodeFormat alone
        // makes the content GS1-compliant.

        int width = switch (codeType) {
            case CODE_128, GS1_128 -> BARCODE_WIDTH;
            case QR_CODE -> QR_CODE_SIZE;
            case GS1_DATA_MATRIX -> DATA_MATRIX_SIZE;
        };

        int height = switch (codeType) {
            case CODE_128, GS1_128 -> BARCODE_HEIGHT;
            case QR_CODE -> QR_CODE_SIZE;
            case GS1_DATA_MATRIX -> DATA_MATRIX_SIZE;
        };

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(codeContent, format, width, height, hints);

            return toPngBytes(matrix);
        } catch (WriterException | IOException e) {
            throw new CodeSymbolGenerationException("Failed to generate code symbol image. Type: " + codeType, e);
        }
    }

    private BarcodeFormat resolveFormat(CodeType codeType) {
        return switch (codeType) {
            case CODE_128, GS1_128 -> BarcodeFormat.CODE_128;
            case QR_CODE -> BarcodeFormat.QR_CODE;
            case GS1_DATA_MATRIX -> BarcodeFormat.DATA_MATRIX;
        };
    }

    private byte[] toPngBytes(BitMatrix matrix) throws IOException {
        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            if (!ImageIO.write(image, "PNG", output)) {
                throw new IOException("PNG image writer is unavailable.");
            }

            return output.toByteArray();
        }
    }
}
