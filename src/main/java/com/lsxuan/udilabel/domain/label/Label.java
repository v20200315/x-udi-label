package com.lsxuan.udilabel.domain.label;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Label {

    private UUID id;
    private UUID productId;
    private String name;
    private String productName;
    private String udiDi;
    private CodeType codeType;
    private String codeContent;
    private String imageUrl;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    protected Label() {
    }

    public static Label create(UUID productId, String name, String productName, String udiDi, CodeType codeType,
        UUID operatorId) {
        validateProductId(productId);
        validateName(name);
        validateCodeType(codeType);
        validateOperatorId(operatorId);

        Label label = new Label();

        LocalDateTime now = LocalDateTime.now();

        label.id = UuidCreator.getTimeOrderedEpoch();
        label.productId = productId;
        label.name = name;
        label.productName = productName;
        label.udiDi = udiDi;
        label.codeType = codeType;
        label.createdAt = now;
        label.createdBy = operatorId;
        label.updatedAt = now;
        label.updatedBy = operatorId;

        return label;
    }

    private static void validateProductId(UUID productId) {

        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be empty.");
        }
    }

    private static void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    private static void validateCodeType(CodeType codeType) {

        if (codeType == null) {
            throw new IllegalArgumentException("codeType cannot be empty.");
        }
    }

    private static void validateOperatorId(UUID operatorId) {

        if (operatorId == null) {
            throw new IllegalArgumentException("operatorId cannot be empty.");
        }
    }
}
