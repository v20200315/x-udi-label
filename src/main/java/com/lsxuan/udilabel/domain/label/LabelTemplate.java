package com.lsxuan.udilabel.domain.label;

import java.util.Arrays;

public enum LabelTemplate {

    BARCODE_10_11("BARCODE_10_11", 1000, 100, CodeType.GS1_128),
    BARCODE_10_17("BARCODE_10_17", 1000, 100, CodeType.GS1_128),
    BARCODE_10_11_17("BARCODE_10_11_17", 1200, 100, CodeType.GS1_128);

    private final String templateCode;
    private final int width;
    private final int height;
    private final CodeType codeType;

    LabelTemplate(String templateCode, int width, int height, CodeType codeType) {
        this.templateCode = templateCode;
        this.width = width;
        this.height = height;
        this.codeType = codeType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public static LabelTemplate fromCode(String templateCode) {
        return Arrays.stream(values()).filter(template -> template.templateCode.equals(templateCode)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported label template encoding:" + templateCode));
    }
}
