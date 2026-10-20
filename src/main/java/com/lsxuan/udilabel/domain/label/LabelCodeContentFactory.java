package com.lsxuan.udilabel.domain.label;

import com.lsxuan.udilabel.domain.product.Product;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LabelCodeContentFactory {

    public String generate(CodeType codeType, Product product) {
        Objects.requireNonNull(codeType, "codeType cannot be null.");
        Objects.requireNonNull(product, "product cannot be null.");

        String udiDi =
            product.getIndicatorDigit() + product.getCompanyPrefix() + product.getItemReference() + product.getCheckDigit();

        if (udiDi.isBlank()) {
            throw new IllegalArgumentException("Product UDI-DI cannot be empty.");
        }

        return switch (codeType) {
            case CODE_128, QR_CODE, GS1_DATA_MATRIX, GS1_128 -> udiDi;
        };
    }
}
