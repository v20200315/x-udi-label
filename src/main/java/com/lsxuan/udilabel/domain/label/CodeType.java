package com.lsxuan.udilabel.domain.label;

/**
 * 标签条码类型。
 */
public enum CodeType {

    /**
     * 普通一维条码。
     */
    CODE_128,

    /**
     * 二维二维码。
     */
    QR_CODE,

    /**
     * GS1 DataMatrix 医疗器械标识码。
     */
    GS1_DATA_MATRIX,

    /**
     * GS1-128 一维条码。
     */
    GS1_128
}