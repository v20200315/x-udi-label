package com.lsxuan.udilabel.domain.label;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LabelTemplate {

    BARCODE_10_11("BARCODE_10_11", 1000, 100, CodeType.GS1_128, "pdf/barcode-label", true, false, true),
    BARCODE_10_17("BARCODE_10_17", 1000, 100, CodeType.GS1_128, "pdf/barcode-label", false, true, true),
    BARCODE_10_11_17("BARCODE_10_11_17", 1200, 100, CodeType.GS1_128, "pdf/barcode-label", true, true, true),

    DM_10_11("DM_10_11", 150, 150, CodeType.GS1_DATA_MATRIX, "pdf/dm-label", true, false, true),
    DM_10_17("DM_10_17", 150, 150, CodeType.GS1_DATA_MATRIX, "pdf/dm-label", false, true, true),
    DM_10_11_17("DM_10_11_17", 150, 150, CodeType.GS1_DATA_MATRIX, "pdf/dm-label", true, true, true);

    private final String templateCode;
    private final int width;
    private final int height;
    private final CodeType codeType;
    private final String htmlTemplate;
    private final boolean includeProductionDate;
    private final boolean includeExpirationDate;
    private final boolean includeLotNumber;

    LabelTemplate(String templateCode, int width, int height, CodeType codeType, String htmlTemplate,
        boolean includeProductionDate, boolean includeExpirationDate, boolean includeLotNumber) {
        this.templateCode = templateCode;
        this.width = width;
        this.height = height;
        this.codeType = codeType;
        this.htmlTemplate = htmlTemplate;
        this.includeProductionDate = includeProductionDate;
        this.includeExpirationDate = includeExpirationDate;
        this.includeLotNumber = includeLotNumber;
    }

    public static LabelTemplate fromCode(String templateCode) {
        return Arrays.stream(values()).filter(template -> template.templateCode.equals(templateCode)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported label template code: " + templateCode));
    }
}