package com.lsxuan.udilabel.interfaces.api.v1.label.dto;

import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.Label;

import java.time.LocalDateTime;
import java.util.UUID;

public record LabelResponse(UUID id, UUID productId, String name, String productName, String udiDi, CodeType codeType,
                            String codeContent, String imageUrl, LocalDateTime createdAt, UUID createdBy,
                            LocalDateTime updatedAt, UUID updatedBy) {

    public static LabelResponse from(Label label) {

        return new LabelResponse(label.getId(), label.getProductId(), label.getName(), label.getProductName(),
            label.getUdiDi(), label.getCodeType(), label.getCodeContent(), label.getImageUrl(), label.getCreatedAt(),
            label.getCreatedBy(), label.getUpdatedAt(), label.getUpdatedBy());
    }
}
