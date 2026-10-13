package com.lsxuan.udilabel.domain.product;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ProductStatus {

    INACTIVE(0), ACTIVE(1);

    @EnumValue
    private final int code;

    ProductStatus(int code) {
        this.code = code;
    }
}
