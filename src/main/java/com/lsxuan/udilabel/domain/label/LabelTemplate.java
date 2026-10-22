package com.lsxuan.udilabel.domain.label;

public enum LabelTemplate {

    // GS1-128 条码
    BARCODE_LOT_PRODUCTION_EXPIRATION(
        "barcode-lot-production-expiration",
        CodeType.GS1_128,
        true,
        true),
    BARCODE_LOT_PRODUCTION(
        "barcode-lot-production",
        CodeType.GS1_128,
        false,
        true),
    BARCODE_LOT_EXPIRATION(
        "barcode-lot-expiration",
        CodeType.GS1_128,
        true,
        false),

    // GS1 DataMatrix
    DATA_MATRIX_LOT_PRODUCTION_EXPIRATION(
        "data-matrix-lot-production-expiration",
        CodeType.GS1_DATA_MATRIX,
        true,
        true),
    DATA_MATRIX_LOT_PRODUCTION(
        "data-matrix-lot-production",
        CodeType.GS1_DATA_MATRIX,
        false,
        true),
    DATA_MATRIX_LOT_EXPIRATION(
        "data-matrix-lot-expiration",
        CodeType.GS1_DATA_MATRIX,
        true,
        false);

    private final String code;
    private final CodeType codeType;
    private final boolean includeExpirationDate;
    private final boolean includeProductionDate;

    LabelTemplate(String code, CodeType codeType, boolean includeExpirationDate, boolean includeProductionDate) {
        this.code = code;
        this.codeType = codeType;
        this.includeExpirationDate = includeExpirationDate;
        this.includeProductionDate = includeProductionDate;
    }

    public String getCode() {
        return code;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public boolean isIncludeExpirationDate() {
        return includeExpirationDate;
    }

    public boolean isIncludeProductionDate() {
        return includeProductionDate;
    }

    public static LabelTemplate fromCode(String code) {
        for (LabelTemplate template : values()) {
            if (template.code.equals(code)) {
                return template;
            }
        }

        throw new IllegalArgumentException("Unsupported label template: " + code);
    }
}
