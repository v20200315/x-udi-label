package com.lsxuan.udilabel.infrastructure.codesymbol;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.LabelTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component("zxingCodeSymbolImageGenerator")
public class ZxingCodeSymbolImageGenerator implements CodeSymbolImageGenerator {

    private static final int BARCODE_WIDTH = 1200;
    private static final int BARCODE_HEIGHT = 100;

    private static final int QR_CODE_SIZE = 300;
    private static final int DATA_MATRIX_SIZE = 300;

    private static final int MARGIN = 2;

    @Override
    public byte[] generate(LabelTemplate labelTemplate, String codeContent) {
        Objects.requireNonNull(labelTemplate, "labelTemplate cannot be null.");

        if (codeContent == null || codeContent.isBlank()) {
            throw new IllegalArgumentException("codeContent cannot be empty.");
        }

        BarcodeFormat format = resolveFormat(labelTemplate.getCodeType());

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, MARGIN);

        if (labelTemplate.getCodeType() == CodeType.QR_CODE) {
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M);
        }

        int width = labelTemplate.getWidth();

        int height = labelTemplate.getHeight();

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(codeContent, format, width, height, hints);
            String svg = toSvg(matrix);
            return svg.getBytes(StandardCharsets.UTF_8);
        } catch (WriterException e) {
            throw new CodeSymbolGenerationException(
                "Failed to generate code symbol image. Type: " + labelTemplate.getTemplateCode(), e);
        }
    }

    private BarcodeFormat resolveFormat(CodeType codeType) {
        return switch (codeType) {
            case CODE_128, GS1_128 -> BarcodeFormat.CODE_128;
            case QR_CODE -> BarcodeFormat.QR_CODE;
            case GS1_DATA_MATRIX -> BarcodeFormat.DATA_MATRIX;
        };
    }

    private String toSvg(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        StringBuilder svg = new StringBuilder();

        svg.append("""
            <?xml version="1.0" encoding="UTF-8"?>
            """);

        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\"").append(" width=\"").append(width).append("\"")
            .append(" height=\"").append(height).append("\"").append(" viewBox=\"0 0 ").append(width).append(" ")
            .append(height).append("\">");

        // White background.
        svg.append("<rect width=\"100%\" height=\"100%\" fill=\"#ffffff\"/>");

        // Black barcode modules.
        svg.append("<path fill=\"#000000\" d=\"");

        for (int y = 0; y < height; y++) {
            int x = 0;

            while (x < width) {
                // Skip white modules.
                while (x < width && !matrix.get(x, y)) {
                    x++;
                }

                if (x >= width) {
                    break;
                }

                // Find a consecutive run of black modules.
                int startX = x;

                while (x < width && matrix.get(x, y)) {
                    x++;
                }

                int runWidth = x - startX;

                // Draw one horizontal rectangle.
                svg.append('M').append(startX).append(' ').append(y).append('h').append(runWidth).append("v1h-")
                    .append(runWidth).append('z');
            }
        }

        svg.append("\"/>");
        svg.append("</svg>");

        return svg.toString();
    }
}
